package own.test.framework;

import java.lang.reflect.Method;
import java.util.List;

public class TestRunner {

    static TestRunResult runTests(String className) throws ClassNotFoundException {
        Class<?> classToRun = Class.forName(className);
        return runTests(classToRun);
    }

    private static TestRunResult runTests(Class<?> classToRun) {
        Method[] methods = classToRun.getMethods();
        List<Method> testMethods = MethodFilterer.filterTestMethods(methods);
        List<Method> beforeMethods = MethodFilterer.filterBeforeMethods(methods);
        List<Method> afterMethods = MethodFilterer.filterAfterMethods(methods);
        return runTestsWithResults(classToRun, testMethods, beforeMethods, afterMethods);
    }

    private static TestRunResult runTestsWithResults(Class<?> classToRun, List<Method> testMethods,
                                                     List<Method> beforeMethods, List<Method> afterMethods) {
        int passed = 0;
        int failed = 0;
        for (Method method : testMethods) {
            try {
                runSingleTest(classToRun, method, beforeMethods, afterMethods);
                passed += 1;
            } catch (Exception e) {
                failed += 1;
                e.getCause();
            }
        }
        return new TestRunResult(passed, failed);
    }

    private static void runSingleTest(Class<?> classToRun, Method testMethod,
                                      List<Method> beforeMethods, List<Method> afterMethods) throws Exception {
        Object testClassObj = classToRun.getConstructor().newInstance();
        for (Method beforeMethod : beforeMethods) {
            beforeMethod.invoke(testClassObj);
        }
        try {
            testMethod.invoke(testClassObj);
        } finally {
            for (Method afterMethod : afterMethods) {
                afterMethod.invoke(testClassObj);
            }
        }
    }
}
