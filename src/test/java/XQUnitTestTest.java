import junit.framework.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * User: ialucas
 * <p/>
 * Test to ensure the XQuery unit test works as expected
 */
public class XQUnitTestTest {


    @Test
    public void sampleTestSuite() throws Exception {
        testXQuery("src/test/xquery/sample/message-business-logic-test.xqy");
    }

    @Test
    public void referenceFunctionalTestSuite() throws Exception {
        testXQuery("src/test/xquery/reference/functional-test.xqy");
    }

    public void testXQuery(final String xQueryTestPath) throws Exception {

        String results = execute(xQueryTestPath);

        Assert.assertTrue(results.contains("pass")); // ensure at least once success
        Assert.assertFalse(results.contains("fail")); // ensure no failures
    }
    

    public String execute(String xQueryPath) throws Exception {

        PrintStream origSystemOut = System.out;

        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();

            System.setOut(new PrintStream(stream));

            net.sf.saxon.Query.main(new String[]{xQueryPath});

            stream.flush();

            return stream.toString();

        } finally {
            System.setOut(origSystemOut);
        }


    }

}
