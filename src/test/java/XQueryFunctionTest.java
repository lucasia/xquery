import com.lucasia.xquery.XQuery;
import junit.framework.Assert;
import net.sf.saxon.query.XQueryExpression;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * User: ialucas
 */
public class XQueryFunctionTest {

    /**
     * TODO: what i'd like to do is display each test method as a seperate test
     * so we can see what is passing and failing
     *
     * @throws Exception
     */
    @Test
    public void testXQuery() throws Exception {
        final String xqFilePath = new String("src/test/xquery/reference/conform-test.xqy");

        XQuery xQuery = new XQuery();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        XQueryExpression expression = xQuery.compileQuery(xqFilePath);

        xQuery.runQuery(expression, new PrintStream(stream));

        final String results = stream.toString();

        Assert.assertTrue(results.contains("pass")); // ensure at least once success
        Assert.assertFalse(results.contains("fail")); // ensure no failures
    }
}
