package automatic_logging.proxy;

import automatic_logging.annotation.Log;
import automatic_logging.app.Logging;
import automatic_logging.app.LoggingImpl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;

public class ProxyLogging {

    public static Logging createLoggingClass() {
        InvocationHandler handler = new LoggingInvocationHandler(new LoggingImpl());

        return (Logging) Proxy.newProxyInstance(ProxyLogging.class.getClassLoader(),
                new Class<?>[]{Logging.class}, handler);
    }

    static class LoggingInvocationHandler implements InvocationHandler {

        private final Object object;

        LoggingInvocationHandler(Object testLogging) {
            this.object = testLogging;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            applyLogAnnotationIfApplicable(method, args);
            return method.invoke(object, args);
        }

        private void printMethodNameWithParams(Method method, Object[] args) {
            printMethodName(method);
            List<Parameter> parameters = Arrays.asList(method.getParameters());
            if (!parameters.isEmpty()) {
                printMethodParams(parameters, args);
            }
            System.out.println();
        }

        private void printMethodName(Method method) {
            System.out.print("execution method: " + method.getName());
        }

        private void printMethodParams(List<Parameter> parameters, Object[] args) {
            System.out.print(",");
            parameters.forEach(p -> {
                System.out.print(" " + p.getName() + ": " + args[parameters.indexOf(p)]);
            });
        }

        private void applyLogAnnotationIfApplicable(Method method, Object[] args) {
            if (method.isAnnotationPresent(Log.class)) {
                printMethodNameWithParams(method, args);
            }
        }
    }
}
