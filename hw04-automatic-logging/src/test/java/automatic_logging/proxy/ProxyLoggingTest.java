package automatic_logging.proxy;

import automatic_logging.annotation.Log;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProxyLoggingTest {

    private TestLogging testLogging;
    private final PrintStream originalOut = System.out;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    static interface TestLogging {
        @Log
        default void methodWithAnnotationAndParams(int param1, String param2) {

        }

        @Log
        default void methodWithAnnotationNoParams() {

        }

        default void methodNoAnnotation() {

        }
    }

    static class TestLoggingImpl implements TestLogging {

    }

    public static TestLogging createLoggingClass() {
        InvocationHandler handler = new ProxyLogging.LoggingInvocationHandler(new TestLoggingImpl());
        return (TestLogging) Proxy.newProxyInstance(ProxyLogging.class.getClassLoader(),
                new Class<?>[]{TestLogging.class}, handler);
    }


    @BeforeEach
    void setUp() {
        this.testLogging = createLoggingClass();
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void testLogAnnotationWithParam() {
        this.testLogging.methodWithAnnotationAndParams(1, "text");
        assertEquals("execution method: methodWithAnnotationAndParams, param1: 1 param2: text\n",
                outContent.toString());
    }

    @Test
    void testLogAnnotationWithNoParam() {
        this.testLogging.methodWithAnnotationNoParams();
        assertEquals("execution method: methodWithAnnotationNoParams\n",
                outContent.toString());
    }

    @Test
    void testLogNoAnnotation() {
        this.testLogging.methodNoAnnotation();
        assertEquals("", outContent.toString());
    }
}