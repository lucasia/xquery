import com.lucasia.xquery.XQueryFileReader;
import junit.framework.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
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
        public boolean accept(final File pathname) {
            return (pathname.isDirectory() || pathname.getName().contains("test"));
        }
    }

}
