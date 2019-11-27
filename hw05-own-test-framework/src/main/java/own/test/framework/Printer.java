package own.test.framework;

public class Printer {

    private static String TEST_RESULTS_MESSAGE_TEMPLATE = "Results:\n Tests run: %d, Failures: %d, Passed: %d";

    static void printTestResults(TestRunResult testRunResult) {
        System.out.println(getTestResultsOutput(testRunResult));
    }

    static String getTestResultsOutput(TestRunResult testRunResult) {
        return String.format(TEST_RESULTS_MESSAGE_TEMPLATE, testRunResult.getTotal(),
                testRunResult.getFailed(), testRunResult.getPassed());
    }
}
