import com.lucasia.xquery.XQuery;
import junit.framework.Assert;
import net.sf.saxon.expr.Expression;
import net.sf.saxon.expr.instruct.Executable;
import net.sf.saxon.functions.FunctionLibrary;
import net.sf.saxon.query.QueryModule;
import net.sf.saxon.query.XQueryExpression;
import net.sf.saxon.query.XQueryFunction;
import net.sf.saxon.query.XQueryFunctionLibrary;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.Set;

/**
 * User: ialucas
 */
public class XQueryFunctionTest {

    /**
     * TODO: what i'd like to do is display each test method as a separate test
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

        /*
        Set<String> localFunctionNames = XQuery.getLocalFunctionNames(expression.getStaticContext());

        for (String localFunctionName : localFunctionNames) {
            System.out.println("localFunctionName = " + localFunctionName);
        }
        */

        xQuery.runQuery(expression, new PrintStream(stream));

        final String results = stream.toString();

        Assert.assertTrue(results.contains("pass")); // ensure at least once success
        Assert.assertFalse(results.contains("fail")); // ensure no failures
    }
}
