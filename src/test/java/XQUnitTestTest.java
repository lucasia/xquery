import com.lucasia.xquery.XQuery;
import junit.framework.Assert;
import org.junit.Test;

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

    @Test
    public void testHelloWorld() throws Exception {
        String results = new XQuery().execute("src/test/xquery/sample/hello-world.xqy");

        System.out.println("results = " + results);
    }


    public void testXQuery(final String xQueryTestPath) throws Exception {

        String results = new XQuery().execute(xQueryTestPath);

        Assert.assertTrue(results.contains("pass")); // ensure at least once success
        Assert.assertFalse(results.contains("fail")); // ensure no failures
    }


}
