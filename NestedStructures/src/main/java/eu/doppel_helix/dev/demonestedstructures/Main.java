package eu.doppel_helix.dev.demonestedstructures;

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

        final DemoLib.gnc_state_t.ByValue state1 = new DemoLib.gnc_state_t.ByValue();
        state1.pos = new double[]{1, 2, 3};
        state1.vel = new double[]{4, 5, 6};
        state1.accel = new double[]{7, 8, 9};

        final DemoLib.gnc_attitude_t.ByValue attitude1 = new DemoLib.gnc_attitude_t.ByValue();
        attitude1.quat = new double[]{10, 11, 12, 13};
        attitude1.rate = new double[]{14, 15, 16};
        attitude1.accel = new double[]{17, 18, 19};

        final DemoLib.gnc_pose_t.ByValue pose1 = new DemoLib.gnc_pose_t.ByValue();
        pose1.state = state1;
        pose1.attitude = attitude1;
        pose1.time = 20;
        pose1.time2 = 21;
        pose1.time3 = 22;

        System.out.println(pose1.size());
        DemoLib.INSTANCE.print_pose(pose1);

//        System.out.println(DemoLib.INSTANCE.return_pose().toString(true));

    }
}
