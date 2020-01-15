This sample shows how to use JNA with the java platform module system (Jigsaw).

The java code can be found in `src/main/java` directory. The project can be
build by running `mvn package` from the main folder. This will result in a
folder `modules` being created in the `target` folder. That folder will hold two
jars:

- jna-5.5.0.jar (module name: com.sun.jna)
- JNA-JPMS-Jigsaw-1.0-SNAPSHOT.jar (module name: eu.doppel_helix.dev.jigsaw)

The demo application can be run by invoking:

```
java -p target/modules -m eu.doppel_helix.dev.jigsaw/eu.doppel_helix.dev.jigsaw.Main
```

This will result in this output:

```
Module of Main class:      eu.doppel_helix.dev.jigsaw
Module of Native class:    com.sun.jna
Module of FileUtils class: com.sun.jna.platform
---------- Packages in the JNA module --------------
com.sun.jna.win32
com.sun.jna
com.sun.jna.ptr
com.sun.jna.internal
---------- Sample Invokation (getHostname) ---------
athena
```

