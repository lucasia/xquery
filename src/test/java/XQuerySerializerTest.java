import com.lucasia.xquery.XQuery;
import com.lucasia.xquery.XQuerySerializer;
import junit.framework.Assert;
import net.sf.saxon.query.XQueryExpression;
import org.junit.Ignore;
import org.junit.Test;

import java.io.*;

public class XQuerySerializerTest {

    @Test
    @Ignore // TODO: close - but still need to fix the FixedElement issue
    public void testSerialize() throws Exception {
        // XQueryExpression expression = new XQuery().compileQuery("src/test/xquery/sample/hello-world.xqy");
        XQueryExpression expression = new XQuery().compileQuery("src/test/xquery/sample/hello-world-flat.xqy");

        StringWriter writer = new StringWriter();

        new XQuerySerializer(writer).serialize(expression);

        final String expected = readFile("src/test/xquery/sample/hello-world-flat.xqy");

        Assert.assertEquals(expected, writer.getBuffer().toString());
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
