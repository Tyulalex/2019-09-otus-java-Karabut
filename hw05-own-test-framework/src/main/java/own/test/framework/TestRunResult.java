package own.test.framework;

public class TestRunResult {

    private int passed;
    private int failed;
    private int total;


    public TestRunResult(int passed, int failed) {
        this.passed = passed;
        this.failed = failed;
        this.total = passed + failed;
    }

    public int getPassed() {
        return passed;
    }

    public int getFailed() {
        return failed;
    }

    public int getTotal() {
        return total;
    }
}
