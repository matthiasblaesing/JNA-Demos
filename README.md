This sample shows how to return a string from a native method and bind it with
JNA.

The native code can be found in `src/main/C` directory.

The java code can be found in `src/main/java` directory.

The sample is runnable, if `make`, `gcc`, `java` and `maven` are on the PATH:

```shell
mvn package exec:java
```