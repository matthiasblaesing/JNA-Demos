
package eu.doppel_helix.dev.jigsaw;

import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.platform.FileUtils;
import com.sun.jna.platform.win32.Winsock2;

public class Main {
    public static void main(String[] args) {
        System.out.println("Module of Main class:      " + Main.class.getModule().getName());
        System.out.println("Module of Native class:    " + Native.class.getModule().getName());
        System.out.println("Module of FileUtils class: " + FileUtils.class.getModule().getName());
        System.out.println("---------- Packages in the JNA module --------------");
        Native.class.getModule().getPackages().forEach(p -> System.out.println(p));
        System.out.println("------- Sample Invokation (getHostname/getUid) -----");
        // The following calls are unsave, unguarded and just for demonstration purposes
        byte[] hostname = new byte[1024];
        if(Platform.isLinux()) {
            com.sun.jna.platform.linux.LibC.INSTANCE.gethostname(hostname, hostname.length);
        } else if (Platform.isMac()) {
            com.sun.jna.platform.mac.SystemB.INSTANCE.gethostname(hostname, hostname.length);
        } else if (Platform.isWindows()) {
            Winsock2.INSTANCE.gethostname(hostname, hostname.length);
        }
        System.out.println("Hostname: '" + Native.toString(hostname) + "'");
    }
}
