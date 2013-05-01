import com.lucasia.xquery.XQuery;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import uk.ac.lkl.common.util.testing.LabelledParameterized;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.lang.String;

public class XQueryTest {


    /**
     * For example
     * "src/test/xquery/sample/message-business-logic-test.xqy"
     * "src/test/xquery/reference/conform-test.xqy"
     * "src/test/xquery/reference/format-test.xqy"
     * "src/test/xquery/reference/functional-test.xqy"
     */
    @Test
    public void testXQueryParams() throws Exception {

        File xqFilePath = new File("src/test/xquery/sample/message-business-logic-test.xqy");

        final String results = new XQuery().execute(xqFilePath);

        Assert.assertTrue(results.contains("pass")); // ensure at least once success
        Assert.assertFalse(results.contains("fail")); // ensure no failures
    }


}