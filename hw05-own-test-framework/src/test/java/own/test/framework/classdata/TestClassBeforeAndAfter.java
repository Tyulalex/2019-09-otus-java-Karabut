package own.test.framework.classdata;

import own.test.framework.annotation.After;
import own.test.framework.annotation.Before;
import own.test.framework.annotation.Test;

public class TestClassBeforeAndAfter implements TestClass {

    @Override
    @Before
    public void method1() {
        System.out.println("executed before method1");
    }

    @Override
    @Before
    public void method2() {
        System.out.println("executed before method2");
    }

    @Override
    @Test
    public void method3() {
        System.out.println("executed test method3");
    }

    @Override
    @Test
    public void method4() {
        System.out.println("executed test method4");
    }

    @Override
    @After
    public void method5() {
        System.out.println("executed after method5");
    }
}
