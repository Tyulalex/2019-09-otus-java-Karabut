package automatic_logging.proxy;

import automatic_logging.app.Logging;

/**
 * Demo of logging methods name and params using ProxyClass.
 * output example:
 * execution method: calculation, var1: fff var2: 123
 */

public class ProxyDemo {


    public static void main(String[] args) {
        Logging testLogging = ProxyLogging.createLoggingClass();
        testLogging.calculation("fff", 123);
    }
}
