import com.lucasia.xquery.XQuery;
import junit.framework.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * User: ialucas
 * <p/>
 * Test to ensure the XQuery unit test works as expected
 */
@Ignore
public class XQueryTest {

    @Test
    public void sampleTestSuite() throws Exception {
        testXQuery("src/test/xquery/sample/message-business-logic-test.xqy");
    }
    
    @Test
    public void referenceConformTestSuite() throws Exception {
        testXQuery("src/test/xquery/reference/conform-test.xqy");
    }
    
    @Test
    public void referenceFormatTestSuite() throws Exception {
        testXQuery("src/test/xquery/reference/format-test.xqy");
    }
    
    @Test
    public void referenceFunctionalTestSuite() throws Exception {
        testXQuery("src/test/xquery/reference/functional-test.xqy");
    }


    public void testXQuery(final String xqFilePath) throws Exception {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        new XQuery().execute(xqFilePath, new PrintStream(stream));

        final String results = stream.toString();

        Assert.assertTrue(results.contains("pass")); // ensure at least once success
        Assert.assertFalse(results.contains("fail")); // ensure no failures
    }


}
