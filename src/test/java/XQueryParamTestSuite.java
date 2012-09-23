import com.lucasia.xquery.XQuery;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.model.FrameworkMethod;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collection;

/**
 * User: ialucas
 * Test to ensure the XQuery unit test works as expected
 */
@RunWith(value = Parameterized.class)
public class XQueryParamTestSuite {

    private String xqFilePath;

    public XQueryParamTestSuite(String xqFilePath) {
        this.xqFilePath = xqFilePath;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        Object[][] data = new Object[][] {
                                    { "src/test/xquery/sample/message-business-logic-test.xqy" },
                                    { "src/test/xquery/reference/conform-test.xqy" },
                                    { "src/test/xquery/reference/format-test.xqy" },
                                    { "src/test/xquery/reference/functional-test.xqy" }
        };

        return Arrays.asList(data);
    }


    @Test
    public void testXQuery() throws Exception {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        new XQuery().execute(xqFilePath, new PrintStream(stream));

        final String results = stream.toString();

        Assert.assertTrue(results.contains("pass")); // ensure at least once success
        Assert.assertFalse(results.contains("fail")); // ensure no failures
    }


}
