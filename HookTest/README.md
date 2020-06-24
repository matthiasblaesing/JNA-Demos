Sample that demonstrates the usage of `MessageLoopThread` (spins the windows
message loop and allows interaction with that thread), `WinEventProc` and
`SetWinEventHook`.

To run it, execute:

```shell
mvn package exec:java
```

Now LOCATIONCHANGE events will be printed. This should look like this:

```
Event 800B, HWDN: native@0x30050
Event 800B, HWDN: native@0x30050
Event 800B, HWDN: native@0x30050
Event 800B, HWDN: null
Event 800B, HWDN: null
Event 800B, HWDN: null
```