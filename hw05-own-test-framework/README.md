# Own test framework

Module implements 3 annotations: Before, After and Test.
It allows to mark methods with such annotations imitate test method and setup and teardown methods.


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

### Testing
Tests are covered by unit test with using junit library


## Acknowledgments

this is done in educational purposes