import com.lucasia.xquery.XQuery;
import com.lucasia.xquery.XQueryFileReader;
import junit.framework.Assert;
import net.sf.saxon.query.XQueryExpression;
import org.junit.Test;

import java.io.*;
import java.util.List;

/**
 * User: ialucas
 */
public class XQueryFileReaderTest {

    public static final File TEST_DIR = new File("src/test/xquery");

    @Test
    public void testListTestClasses() throws FileNotFoundException {
        List<File> fileNames = new XQueryFileReader().recursiveList(TEST_DIR, new XQueryTestFileFilter());

        Assert.assertTrue(!fileNames.isEmpty());
    }

    public static class XQueryTestFileFilter implements FileFilter {
        @Override
        public boolean accept(File pathname) {
            return (pathname.isDirectory() || pathname.getName().contains("test"));
        }
    }

}
