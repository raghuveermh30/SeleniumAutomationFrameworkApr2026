package listeners;

import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * TestNG annotation transformer that automatically attaches the
 * {@link Retry} retry analyzer to every test method in the suite.
 */
public class AnnotationTransformer implements IAnnotationTransformer {

    /**
     * Assigns {@link Retry} as the retry analyzer for the given test annotation.
     *
     * @param annotation the TestNG test annotation being transformed
     * @param testClass the test class, if annotated at class level
     * @param testConstructor the test constructor, if annotated at constructor level
     * @param testMethod the test method, if annotated at method level
     */
    @Override
    public void transform(ITestAnnotation annotation,
                          Class testClass,
                          Constructor testConstructor,
                          Method testMethod) {
        annotation.setRetryAnalyzer(Retry.class);
    }
}
