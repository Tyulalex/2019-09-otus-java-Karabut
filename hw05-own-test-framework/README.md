# Own test framework

Module implements 3 annotations: Before, After and Test.
It has TestRunner that takes path to class marked with such annotations as argument
and run all methods marked with annotations in order:
Before(s), Test , After(s). if any of test method throws exception - test marked as 
failed. After method always executed.
Print result of test execution

## Getting Started

jdk 12+ required
mvn package


### Run
own.test.framework.Main is a main class that run tests
-ea shall be passed as VM options 
program takes -c <path to class> 
example: 
```
-ea -c own.test.framework.tests.DIYMathTest
```

output example
```
Results:
 Tests run: 3, Failures: 1, Passed: 2
```

### Testing
Tests are covered by unit test with using junit library


## Acknowledgments

this is done in educational purposes