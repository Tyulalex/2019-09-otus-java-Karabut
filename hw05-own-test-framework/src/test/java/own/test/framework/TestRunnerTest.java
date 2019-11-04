package own.test.framework;

import org.junit.jupiter.api.Test;
import own.test.framework.annotation.After;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestRunnerTest extends BaseTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @Test
    void runTestsNoTests() throws Exception {
        TestRunResult result = TestRunner.runTests(CLASS_NO_TESTS);
        assertEquals(0, result.getTotal());
        assertEquals(0, result.getPassed());
        assertEquals(0, result.getFailed());
    }

    @Test
    void runTestsAllPassedTests() throws Exception {
        TestRunResult result = TestRunner.runTests(TEST_CLASS_PASSED);
        assertEquals(2, result.getTotal());
        assertEquals(2, result.getPassed());
        assertEquals(0, result.getFailed());
    }

    @Test
    void runTestsFailedTests() throws Exception {
        TestRunResult result = TestRunner.runTests(TEST_CLASS_FAILED);
        assertEquals(2, result.getTotal());
        assertEquals(1, result.getPassed());
        assertEquals(1, result.getFailed());
    }

    @Test
    void runTestsWithBeforeAndAfter() throws Exception {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
        TestRunResult result = TestRunner.runTests(TEST_CLASS_BEFORE_AND_AFTER);
        assertEquals(2, result.getPassed());
        assertEquals("executed before method1\n" +
                "executed before method2\n" +
                "executed test method3\n" +
                "executed after method5\n" +
                "executed before method1\n" +
                "executed before method2\n" +
                "executed test method4\n" +
                "executed after method5\n", outContent.toString());
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @After
    void tearDown() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }
}