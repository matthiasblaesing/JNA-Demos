package eu.doppel_helix.dev.demoglobalconststruct;

import com.sun.jna.Pointer;
import java.io.File;

public class Main {

    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public static void main(String[] args) {
        // Ensure, that the directory, the native library is build to is on
        // the JNA search path
        File nativeDir = new File("target/native");
        System.setProperty("jna.library.path", nativeDir.getAbsolutePath());

        Pointer pointPointer = DemoLib.LIBRARY.getGlobalVariableAddress("mypoint");
        DemoLib.Point mypoint = new DemoLib.Point(pointPointer);
        System.out.println(mypoint);
        DemoLib.Point result = DemoLib.INSTANCE.translate(mypoint, 100, 100);
    }
}