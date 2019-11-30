package eu.doppel_helix.dev.demoreturnstring;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;

public interface DemoLib extends Library {

    DemoLib INSTANCE = Native.load("demolib", DemoLib.class);

    NativeLong demoString(byte[] result);
    NativeLong demoString(Pointer result);
}
