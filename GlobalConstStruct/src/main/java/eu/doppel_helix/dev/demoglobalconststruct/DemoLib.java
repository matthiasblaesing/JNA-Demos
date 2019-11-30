package eu.doppel_helix.dev.demoglobalconststruct;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;

public interface DemoLib extends Library {
    NativeLibrary LIBRARY = NativeLibrary.getInstance("demolib");
    DemoLib INSTANCE = Native.load("demolib", DemoLib.class);

    @Structure.FieldOrder({"x", "y"})
    class Point extends Structure {

        public volatile int x, y;

        public Point() {
        }

        public Point(Pointer p) {
            super(p);
            read();
        }
    }

    Point translate(Point pt, int x, int y);
}