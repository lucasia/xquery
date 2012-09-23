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

        final String actualContent = writer.getBuffer().toString();

        // make sure the flattened version is what we expect
        final String expectedContent = XQueryFlattener.readFile("src/test/xquery/sample/hello-world-flat.xqy");
        Assert.assertEquals(expectedContent, actualContent);

        // check if we can execute the flattened xquery
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(actualContent.getBytes());
        ByteArrayOutputStream actualXQResult = new ByteArrayOutputStream();
        new XQuery().execute(inputStream, new PrintStream(actualXQResult));

        // make sure that it can also be executed
        Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?><result><msg>hello world!</msg></result>", actualXQResult.toString());
    }


    @Test
    public void testBigFlatten() throws Exception {
        final XQueryExpression expression = new XQuery().compileQuery("src/test/xquery/sample/reverse.xqy");

        StringWriter writer = new StringWriter();

        new XQueryFlattener(writer).flatten(expression);

        final String actualContent = writer.getBuffer().toString();

        // make sure the flattened version is what we expect
        final String expectedContent = XQueryFlattener.readFile("src/test/xquery/sample/reverse-flat.xqy");
        Assert.assertEquals(expectedContent, actualContent);

        // check if we can execute the flattened xquery
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(actualContent.getBytes());
        ByteArrayOutputStream actualXQResult = new ByteArrayOutputStream();
        new XQuery().execute(inputStream, new PrintStream(actualXQResult));

        // make sure that it can also be executed
        Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?><result>sdrawkcab</result>", actualXQResult.toString());

    }


}
