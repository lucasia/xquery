import com.lucasia.xquery.XQuery;
import com.lucasia.xquery.XQuerySerializer;
import junit.framework.Assert;
import net.sf.saxon.query.XQueryExpression;
import org.junit.Ignore;
import org.junit.Test;

import java.io.*;

public class XQuerySerializerTest {

    @Test
    public void testSerialize() throws Exception {
        final XQueryExpression expression = new XQuery().compileQuery("src/test/xquery/sample/hello-world.xqy");

        StringWriter writer = new StringWriter();

        new XQuerySerializer(writer).serialize(expression);

        String expectedPath = "src/test/xquery/sample/hello-world-flat.xqy";
        final String expected = readFile(expectedPath);

        // make sure the flat is what we expect
        Assert.assertEquals(expected, writer.getBuffer().toString());

        // make sure that it can also be executed
        Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?><result><foo>hello world!</foo></result>",
                new XQuery().execute(expectedPath));
    }




    public String readFile(String filePath) throws IOException {

        final FileReader fileReader = new FileReader(filePath);

        final BufferedReader bufferedReader = new BufferedReader(fileReader);

        final StringBuffer buffer = new StringBuffer();

        String line;
        while ((line = bufferedReader.readLine()) != null) {
            buffer.append(line).append("\n");
        }

        fileReader.close();

        return buffer.toString();
    }


}
