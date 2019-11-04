package own.test.framework;

import own.test.framework.annotation.After;
import own.test.framework.annotation.Before;
import own.test.framework.annotation.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MethodFilterer {

    static List<Method> filterTestMethods(Method[] methods) {
        return filterMethods(methods, Test.class);
    }

    static List<Method> filterBeforeMethods(Method[] methods) {
        return filterMethods(methods, Before.class);
    }

    static List<Method> filterAfterMethods(Method[] methods) {
        return filterMethods(methods, After.class);
    }

    static List<Method> filterMethods(Method[] methods, Class<? extends Annotation> annotation) {
        List<Method> filteredMethods = new ArrayList<>();
        for (Method method : methods) {
            if (method.isAnnotationPresent(annotation)) {
                filteredMethods.add(method);
            }
        }
        return filteredMethods;
    }
}
