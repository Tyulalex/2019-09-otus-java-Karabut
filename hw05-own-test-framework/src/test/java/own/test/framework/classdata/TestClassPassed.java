package own.test.framework.classdata;

import own.test.framework.annotation.After;
import own.test.framework.annotation.Before;
import own.test.framework.annotation.Test;

public class TestClassPassed implements TestClass {

    @Override
    @Test
    public void method1() {
    }

    ;

    @Override
    @Test
    public void method2() {
    }

    ;

    @Override
    @Before
    public void method3() {
    }

    ;

    @Override
    @After
    public void method4() {
    }

    ;
}
