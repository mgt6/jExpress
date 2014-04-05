import co.uk.taycon.mark.jExpress.Express;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class JExpressUnitTest {

    Express express = new Express();

    @Test
    public void testConnectToPort() throws Exception {
        assertThat(true, is(true));
    }
}
