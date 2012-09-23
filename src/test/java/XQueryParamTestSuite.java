import com.lucasia.xquery.XQuery;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import uk.ac.lkl.common.util.testing.LabelledParameterized;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;

/**
 * User: ialucas
 * Test to ensure the XQuery unit test works as expected
 */
@RunWith(value = LabelledParameterized.class)
public class XQueryParamTestSuite {

    private String xqFilePath;
    private String label;

    public XQueryParamTestSuite(String label, String xqFilePath) {
        this.label = label;
        this.xqFilePath = xqFilePath;
    }


    @Parameterized.Parameters
    public static Collection<Object[]> getParameters() {
        Collection<Object[]> parameters = new ArrayList<Object[]>();

        parameters.add(new Object[]{"message-business-logic-test.xqy", "src/test/xquery/sample/message-business-logic-test.xqy"});
        parameters.add(new Object[]{"conform-test.xqy", "src/test/xquery/reference/conform-test.xqy"});
        parameters.add(new Object[]{"format-test.xqy", "src/test/xquery/reference/format-test.xqy"});
        parameters.add(new Object[]{"functional-test.xqy", "src/test/xquery/reference/functional-test.xqy"});

        return parameters;

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
