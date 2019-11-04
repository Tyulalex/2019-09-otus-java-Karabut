package own.test.framework;

import org.apache.commons.cli.*;

public class Main {

    public static void main(String[] args) {
        try {
            TestRunResult result = TestRunner.runTests(parseArgs(args).getOptionValue("classname"));
            Printer.printTestResults(result);
        } catch (MissingOptionException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static Options buildOptions() {
        Options options = new Options();
        options.addRequiredOption(
                "c", "classname", true, "Class Name with package");
        return options;
    }

    private static CommandLine parseArgs(String[] args) throws ParseException {
        Options options = buildOptions();
        CommandLineParser parser = new DefaultParser();
        return parser.parse(options, args);
    }
}
