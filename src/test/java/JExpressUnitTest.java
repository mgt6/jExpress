import co.uk.taycon.mark.jExpress.Express;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class JExpressUnitTest {

    private static Express express = new Express();

    private static final int TEST_PORT = 9998;

    private static final String TEST_HOST = "localhost";

    private static final String TEST_PATH = "/test";

    private HttpGet getTestPath = new HttpGet("http://" + TEST_HOST + ":" + TEST_PORT + TEST_PATH);

    private DefaultHttpClient client = new DefaultHttpClient();

    @AfterClass
    public static void tearDown() throws Exception {
        express.stop();
    }

    @BeforeClass
    public static void setUp() throws Exception {
        express.listen(TEST_PORT);
    }

    @Test
    public void testGetSendMessage() throws IOException {

        String testMessage = "this is a test message";

        express.get("/test", (req, res) ->{

            assertThat(req.getRequestHeaderMap().containsKey("User-Agent"), is(true));
            assertThat(req.getRequestHeaderMap().get("User-Agent"), containsString("Apache-HttpClient"));
            assertThat(req.getRequestHeaderMap().containsKey("Host"), is(true));
            assertThat(req.getRequestHeaderMap().get("Host"), is(TEST_HOST));

            res.send(testMessage);
        });

        HttpResponse response = client.execute(getTestPath);

        BufferedReader br = new BufferedReader(
                new InputStreamReader((response.getEntity().getContent())));

        String output;
        while ((output = br.readLine()) != null) {
            assertThat(output, is(testMessage));
        }

        assertThat(response.getAllHeaders().length, not(0));
        assertThat(response.getStatusLine().toString(), is("HTTP/1.1 200 OK"));

        client.getConnectionManager().shutdown();

    }

    @Test
    public void testGetSendStatus() throws IOException {

        int errorCode = 403;

        express.get("/test", (req, res) ->{

            assertThat(req.getRequestHeaderMap().containsKey("User-Agent"), is(true));
            assertThat(req.getRequestHeaderMap().get("User-Agent"), containsString("Apache-HttpClient"));
            assertThat(req.getRequestHeaderMap().containsKey("Host"), is(true));
            assertThat(req.getRequestHeaderMap().get("Host"), is(TEST_HOST));

            res.send(403);
        });

        HttpResponse response = client.execute(getTestPath);

        assertThat(response.getAllHeaders().length, not(0));
        assertThat(response.getStatusLine().toString(), is("HTTP/1.1 " + errorCode + " "));

        client.getConnectionManager().shutdown();

    }

    @Test
    public void testGetSendMessageAndStatus() throws IOException {

        int errorCode = 403;

        String testMessage = "naw!";

        express.get("/test", (req, res) ->{

            assertThat(req.getRequestHeaderMap().containsKey("User-Agent"), is(true));
            assertThat(req.getRequestHeaderMap().get("User-Agent"), containsString("Apache-HttpClient"));
            assertThat(req.getRequestHeaderMap().containsKey("Host"), is(true));
            assertThat(req.getRequestHeaderMap().get("Host"), is(TEST_HOST));

            res.send(403, testMessage);
        });

        HttpResponse response = client.execute(getTestPath);

        BufferedReader br = new BufferedReader(
                new InputStreamReader((response.getEntity().getContent())));


        String output;
        while ((output = br.readLine()) != null) {
            assertThat(output, is(testMessage));
        }

        assertThat(response.getAllHeaders().length, not(0));
        assertThat(response.getStatusLine().toString(), is("HTTP/1.1 " + errorCode + " "));

        client.getConnectionManager().shutdown();

    }
}
