# Automatic Logging

this module demonstrates functionality of logging 
method name and arguments values passed to this method.
If the method is marked by @Log annotation
such information will be logged 

Two implementations provided
1. using ASM library and java agent
2. using Proxy Class Loaders

## Getting Started

jdk 12+ needed
mvn package

### Using JAVA Agent 
Assume we have written our method call
```
Logging testLogging = new LoggingImpl();
testLogging.calculation("fff", 123);
```
then run our program
```
java -javaagent:target/LogAgent.jar -jar target/LogAgent.jar
```
output example:
```
executed method: calculation, params: fff;123;
```
#### Comments for JAVA Agent
 - no tests
 - cannot print method signature arg names
 - work only for LoggingImpl class 
 
### Using Proxy Class 
Assume we have located in main method the certain code: 
```
Logging testLogging = ProxyLogging.createLoggingClass();
testLogging.calculation("fff", 123);
```
then run our program 
output example:
```
execution method: calculation, var1: fff var2: 123
```
#### Comments for Proxy Class
 - covered with unit tests
 - work only for LoggingImpl class 
 - need to create instance using static method     


