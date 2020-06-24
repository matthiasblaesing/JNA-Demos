package eu.doppel_helix.dev.demohooktest;

import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.User32Util;
import com.sun.jna.platform.win32.WinDef.DWORD;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.LONG;
import com.sun.jna.platform.win32.WinNT.HANDLE;
import com.sun.jna.platform.win32.WinUser.WinEventProc;
import java.lang.ref.Reference;
import java.util.concurrent.Callable;

public class Main {

    public static void main(String[] args) throws Exception {
        WinEventProc winEventProc[] = new WinEventProc[1];
        // Prepare the message loop thread
        User32Util.MessageLoopThread mlt = new User32Util.MessageLoopThread();
        mlt.start();
        // Set the WinEventHook in the message loop thread
        mlt.runOnThread(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                System.out.println("Prepare the WinEventProc");

                winEventProc[0] = new WinEventProc() {

                    @Override
                    public void callback(HANDLE hWinEventHook, DWORD event, HWND hwnd, LONG idObject, LONG idChild, DWORD dwEventThread, DWORD dwmsEventTime) {
                        System.out.printf("Event %1$X, HWDN: %2$s%n", event.intValue(), hwnd);
                    }
                };

                // Register the WinEventHook for all processes and listen only
                // to LOCATIONCHANGE events. The listener can't be loaded into
                // the target process so WINEVENT_OUTOFCONTEXT is specified
                int WINEVENT_OUTOFCONTEXT = 0x0000;
                int WINEVENT_SKIPOWNPROCESS = 0x0002;

                int EVENT_OBJECT_LOCATIONCHANGE = 0x800B;       // hwnd ID idChild is moved/sized item

                HANDLE hhk = User32.INSTANCE.SetWinEventHook(EVENT_OBJECT_LOCATIONCHANGE, EVENT_OBJECT_LOCATIONCHANGE, null, winEventProc[0], 0, 0, WINEVENT_OUTOFCONTEXT | WINEVENT_SKIPOWNPROCESS);
                System.out.printf("EventHook Handle: %s%n", hhk.toString());
                return null;
            }
        });
        mlt.join();
        // Ensure the WinEventProc is not collected before the programm ends
        Reference.reachabilityFence(winEventProc[0]);
    }
}
