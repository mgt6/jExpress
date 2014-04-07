import org.apache.http.HttpResponse;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class JExpressGetRouteTest extends AbstractJExpressTest {

    @Test
    public void testGetSendMessage() throws IOException {

        String testMessage = "this is a test message";

        express.get(TEST_PATH, (req, res) ->{

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

        express.get(TEST_PATH, (req, res) ->{

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

        express.get(TEST_PATH, (req, res) ->{

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

    @Test
    public void testStaticRoute() throws IOException {

        String resourcePath = System.getProperty("user.dir") + "/src/test/resources/html/index.html";

        express.staticResource(TEST_PATH, resourcePath);

        HttpResponse response = client.execute(getTestPath);

        BufferedReader br = new BufferedReader(
                new InputStreamReader((response.getEntity().getContent())));

        List<String> lines = Files.readAllLines(Paths.get(resourcePath));

        String output;
        while ((output = br.readLine()) != null) {
            assertThat(output, is(lines.remove(0)));
        }

        assertThat(response.getAllHeaders().length, not(0));
        assertThat(response.getStatusLine().toString(), is("HTTP/1.1 200 "));

        client.getConnectionManager().shutdown();


    }
}
