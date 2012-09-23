import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;

/**
 * User: ialucas
 */
public class XQueryTestSuite extends Suite {

    public XQueryTestSuite(Class<?> clazz, RunnerBuilder builder) throws InitializationError {
        this(builder, clazz, getSuiteClass());
    }

    public XQueryTestSuite(RunnerBuilder builder, Class<?> clazz, Class<?>[] suiteClasses) throws InitializationError {
        super(builder, clazz, suiteClasses);
    }

    public static Class<?>[] getSuiteClass() {
        return new Class[] {XQueryParamTestSuite.class};
    }

}
