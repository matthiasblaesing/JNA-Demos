This sample binds the quickbooks QBXMLRP2 RequestProcessor COM typelib.

The bindings in the package eu.doppel_helix.jna.tlb.quickbooks are generated
with TlbCodeGenerator using:

```shell
mvn eu.doppel-helix.jna.tlbcodegenerator:TlbCodeGenerator:generate
```

For this to work the typelibrary `QBXMLRP2.dll` must be placed in `src/main/dll`.

As the code can only be generated on windows and the library can't be
distributed, it is checked in.

The sample is untested, but should be runnable if quickbooks is installed on
the system.

```shell
mvn package exec:java
```