package own.test.framework.tests;

import own.test.framework.annotation.After;
import own.test.framework.annotation.Before;
import own.test.framework.annotation.Test;
import own.test.framework.app.DIYMath;

public class DIYMathTest {
    @Before
    public void setUp() {
        System.out.println("Before");
    }

    @Test
    public void testSumError() throws Exception {
        assert DIYMath.sum(3, 6) == 2;
    }


    @Test
    public void testSumZero() throws Exception {
        assert DIYMath.sum(0, 0) == 0;
    }

    @Test
    public void testSubSuccess() throws Exception {
        assert DIYMath.sub(3, 2) == 1;
    }

    @After
    public void tearDown() {
        System.out.println("After");
    }
}
