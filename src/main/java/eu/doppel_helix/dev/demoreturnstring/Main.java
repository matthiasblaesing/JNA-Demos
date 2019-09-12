package eu.doppel_helix.dev.demoreturnstring;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import java.io.File;

public class Main {

    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public static void main(String[] args) {
        // Ensure, that the directory, the native library is build to is on
        // the JNA search path
        File nativeDir = new File("target/native");
        System.setProperty("jna.library.path", nativeDir.getAbsolutePath());

        // Invoke the demo function with a NULL buffer to get the required
        // buffer size
        int length = DemoLib.INSTANCE.demoString((byte[]) null).intValue();

        System.out.printf("%nNeeded Buffer: %d%n%n", length);

        System.out.println("============ Byte Array ============");

        byte[] resultByteArray = new byte[length];
        DemoLib.INSTANCE.demoString(resultByteArray);
        System.out.println(Native.toString(resultByteArray));

        System.out.println("============   Memory   ============");

        Memory resultMemory = new Memory(length);
        DemoLib.INSTANCE.demoString(resultMemory);
        System.out.println(resultMemory.getString(0));
    }
}
