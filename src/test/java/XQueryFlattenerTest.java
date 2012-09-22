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

        final String expected = XQueryFlattener.readFile("src/test/xquery/sample/hello-world-flat.xqy");

        // make sure the flattened version is what we expect
        final String actual = writer.getBuffer().toString();
        Assert.assertEquals(expected, actual);

        // check if we can execute the flattened xquery
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(actual.getBytes());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        new XQuery().execute(inputStream, new PrintStream(outputStream));

        // make sure that it can also be executed
        Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?><result><msg>hello world!</msg></result>", outputStream.toString());
    }


}
