package own.test.framework.classdata;

import own.test.framework.annotation.After;
import own.test.framework.annotation.Test;

public class TestClassFailed implements TestClass {

    @Override
    @Test
    public void method1() throws Exception {
        throw new Exception();
    }

    @Override
    @Test
    public void method2() {
    }

    @Override
    @After
    public void method3(){
        System.out.println("afterMethodExecuted");
    }
}
