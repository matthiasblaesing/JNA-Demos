NOTE: At time of writing, the function required for this test is not yet
      integrated into JNA and thus needs a custom build!

--------------------------------------------------------------------------------

This sample shows how the RegisterApplicationRestart function can be used to
automatically restart a crashing application.

1. Run `mvn package` to build the application
2. Move the binary from `target/ApplicationRestart.jar` to `c:\temp\` (or change
   the sourcecode to point to the location of that file)
3. Run `javaw -XX:+UseOSErrorReporting -jar C:\temp\ApplicationRestart.jar`
   (also works with java as launcher)
4. Wait at least 60s
5. Klick the "Kill" Button

The process will crash and will be restarted. The restarted process can be
idenfied by looking at the "Arguments" entry, which now holds the value
"restarted".

The "-XX:+UseOSErrorReporting" argument is necessary to disable the default
java crash handling, which interferes with the windows crash handling.

In the tests calls to "UnregisterApplicationRestart" were not necessary, as a
clean exit (closing the window) did not trigger a restart.