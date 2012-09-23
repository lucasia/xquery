import com.lucasia.xquery.*;
import com.lucasia.xquery.XQueryFileReader;
import junit.framework.Assert;
import net.sf.saxon.query.XQueryExpression;
import org.junit.Test;

import java.io.*;

public class XQueryFlattenerTest {

    @Test
    public void testFlatten() throws Exception {
        final XQueryExpression expression = new XQuery().compileQuery("src/test/xquery/sample/hello-world.xqy");

        StringWriter writer = new StringWriter();

        new XQueryFlattener(writer).flatten(expression);

        final String actualContent = writer.getBuffer().toString();

        // make sure the flattened version is what we expect
        final String expectedContent = new XQueryFileReader().readFile("src/test/xquery/sample/hello-world-flat.xqy");
        Assert.assertEquals(expectedContent, actualContent);

        // check if we can execute the flattened xquery
        String actualXQResult = new XQuery().execute(actualContent);

        final String expected =
                        "<result>\n" +
                        "   <msg>hello world!</msg>\n" +
                        "</result>";

        // make sure that it can also be executed
        Assert.assertEquals(expected, actualXQResult);
    }


    @Test
    public void testBigFlatten() throws Exception {
        final XQueryExpression expression = new XQuery().compileQuery("src/test/xquery/sample/reverse.xqy");

        StringWriter writer = new StringWriter();

        new XQueryFlattener(writer).flatten(expression);

        final String actualContent = writer.getBuffer().toString();

        // make sure the flattened version is what we expect
        final String expectedContent = new XQueryFileReader().readFile("src/test/xquery/sample/reverse-flat.xqy");
        Assert.assertEquals(expectedContent, actualContent);

        // check if we can execute the flattened xquery
        String actualXQResult = new XQuery().execute(actualContent);

        // make sure that it can also be executed
        Assert.assertEquals("<result>sdrawkcab</result>", actualXQResult);

    }


}
