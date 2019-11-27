package own.test.framework;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MethodFiltererTest extends BaseTest {

    private Class<?> testClassPassed;
    private Class<?> noTestsClass;

    @BeforeEach
    void setUp() throws Exception {
        testClassPassed = Class.forName(TEST_CLASS_PASSED);
        noTestsClass = Class.forName(CLASS_NO_TESTS);
    }


    @Test
    void filterTestMethodsMany() throws Exception {
        List<Method> expectedTestMethods = List.of(
                testClassPassed.getMethod("method1"),
                testClassPassed.getMethod("method2")
        );
        List<Method> actualTestMethods = MethodFilterer.filterTestMethods(
                testClassPassed.getMethods());
        assertEquals(expectedTestMethods, actualTestMethods);
    }

    @Test
    void filterTestMethodsNo() throws Exception {
        List<Method> expectedTestMethods = List.of();
        List<Method> actualTestMethods = MethodFilterer.filterTestMethods(
                noTestsClass.getMethods());
        assertEquals(expectedTestMethods, actualTestMethods);
    }

    @Test
    void filterBeforeMethods() throws Exception {
        List<Method> expectedBeforeMethods = List.of(
                testClassPassed.getMethod("method3")
        );
        List<Method> actualBeforeMethods = MethodFilterer.filterBeforeMethods(
                testClassPassed.getMethods());
        assertEquals(expectedBeforeMethods, actualBeforeMethods);
    }

    @Test
    void filterBeforeMethodsNo() throws Exception {
        List<Method> expectedBeforeMethods = List.of();
        List<Method> actualBeforeMethods = MethodFilterer.filterBeforeMethods(
                noTestsClass.getMethods());
        assertEquals(expectedBeforeMethods, actualBeforeMethods);
    }

    @Test
    void filterAfterMethods() throws Exception {
        List<Method> expectedAfterMethods = List.of(
                testClassPassed.getMethod("method4")
        );
        List<Method> actualAfterMethods = MethodFilterer.filterAfterMethods(
                testClassPassed.getMethods());
        assertEquals(expectedAfterMethods, actualAfterMethods);
    }

    @Test
    void filterAfterMethodsNo() throws Exception {
        List<Method> expectedAfterMethods = List.of();
        List<Method> actualAfterMethods = MethodFilterer.filterAfterMethods(
                noTestsClass.getMethods());
        assertEquals(expectedAfterMethods, actualAfterMethods);
    }
}