import com.lucasia.xquery.XQuery;
import com.lucasia.xquery.XQueryFileReader;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import uk.ac.lkl.common.util.testing.LabelledParameterized;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * User: ialucas
 * Test to ensure the XQuery unit test works as expected
 */
@RunWith(value = LabelledParameterized.class)
public class XQueryParamTest {

    private String xqFilePath;
    private String label;

    public XQueryParamTest(String label, String xqFilePath) {
        this.label = label;
        this.xqFilePath = xqFilePath;
    }


    @Parameterized.Parameters
    public static Collection<Object[]> getParameters() throws FileNotFoundException {
        Collection<Object[]> parameters = new ArrayList<Object[]>();

        XQueryFileReader fileReader = new XQueryFileReader();

        List<File> files = fileReader.recursiveList(XQueryFileReaderTest.TEST_DIR, new XQueryFileReaderTest.XQueryTestFileFilter());

        for (File file : files) {
            String testPath = file.getAbsolutePath();
            String label = file.getName();

            parameters.add(new Object[]{label, testPath});
        }

        return parameters;
    }


    @Test
    public void testXQuery() throws Exception {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        new XQuery().execute(xqFilePath, new PrintStream(stream));

        final String results = stream.toString();

        Assert.assertTrue(results.contains("pass")); // ensure at least once success
        Assert.assertFalse(results.contains("fail")); // ensure no failures
    }


}
