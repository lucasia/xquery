import com.lucasia.xquery.XQuery;
import com.lucasia.xquery.XQueryFileReader;
import com.lucasia.xquery.XQueryFlattener;
import junit.framework.Assert;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XQueryExecutable;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

public class XQueryFlattenerTest {

    @Test
    public void testFlatten() throws Exception {
        final File fileToFlatten = new File("src/test/xquery/sample/hello-world.xqy");

        final File expectedFileResult = new File("src/test/xquery/sample/hello-world-flat.xqy");

        final String expectedXQResult =
                        "<result>\n"
                        + "   <msg>hello world!</msg>\n"
                        + "</result>";


        testFlatten(fileToFlatten, expectedFileResult, expectedXQResult);
    }


    @Test
    public void testBigFlatten() throws Exception {

        final File fileToFlatten = new File("src/test/xquery/sample/reverse.xqy");

        final File expectedFileResult = new File("src/test/xquery/sample/reverse-flat.xqy");

        final String expectedXQResult = "<result>sdrawkcab</result>";

        testFlatten(fileToFlatten, expectedFileResult, expectedXQResult);

    }

    public void testFlatten(final File fileToFlatten, final File expectedFileResult, final String expectedXQueryResult)
            throws IOException, SaxonApiException {
        final XQueryExecutable xQueryExecutable = new XQuery().compileQuery(fileToFlatten);

        StringWriter writer = new StringWriter();

        new XQueryFlattener(writer).flatten(xQueryExecutable);

        final String actualContent = writer.getBuffer().toString();

        // make sure the flattened version is what we expect
        final String expectedContent = new XQueryFileReader().readFile(expectedFileResult);
        Assert.assertEquals(expectedContent, actualContent);

        // check if we can execute the flattened xquery
        String actualXQResult = new XQuery().execute(actualContent);

        // make sure that it can also be executed
        Assert.assertEquals(expectedXQueryResult, actualXQResult);
    }


}
