import org.apache.http.HttpResponse;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

public class JExpressStaticRouteTest extends AbstractJExpressTest {

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
