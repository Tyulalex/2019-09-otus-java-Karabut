package automatic_logging.agent;

import automatic_logging.app.Logging;
import automatic_logging.app.LoggingImpl;


/*
    java -javaagent:target/LogAgent.jar -jar target/LogAgent.jar
    output example:
    executed method: calculation, params: fff;123;

*/


public class Demo {

    public static void main(String[] args) {
        Logging testLogging = new LoggingImpl();
        testLogging.calculation("fff", 123);
        testLogging.calculation2("sss");
        testLogging.calculation3();
    }
}
