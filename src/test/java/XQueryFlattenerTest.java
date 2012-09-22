import com.lucasia.xquery.XQuery;
import com.lucasia.xquery.XQueryFlattener;
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

        String expectedPath = "src/test/xquery/sample/hello-world-flat.xqy";
        final String expected = XQueryFlattener.readFile(expectedPath);

        // make sure the flat is what we expect
        Assert.assertEquals(expected, writer.getBuffer().toString());

        // make sure that it can also be executed
        Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?><result><msg>hello world!</msg></result>",
                new XQuery().execute(expectedPath));
    }


}
