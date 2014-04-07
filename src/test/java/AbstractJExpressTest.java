import co.uk.taycon.mark.jExpress.Express;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.AfterClass;
import org.junit.BeforeClass;

public abstract class AbstractJExpressTest {

    protected static final int TEST_PORT = 9998;

    protected static final String TEST_HOST = "localhost";

    protected static final String TEST_PATH = "/test";

    protected HttpGet getTestPath = new HttpGet("http://" + TEST_HOST + ":" + TEST_PORT + TEST_PATH);

    protected DefaultHttpClient client = new DefaultHttpClient();

    protected static Express express = new Express();

    @AfterClass
    public static void tearDown() throws Exception {
        express.stop();
    }

    @BeforeClass
    public static void setUp() throws Exception {
        express.listen(TEST_PORT);
    }

}
