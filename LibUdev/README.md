This sample shows device monitoring using libudev. It uses the udev netlink
functionality to receive events and outputs the metadata.

It can be run from CLI by running:

```
mvn package
java -jar target/LibUdev.jar
```