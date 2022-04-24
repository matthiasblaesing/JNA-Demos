package eu.doppel_helix.dev.demonestedstructures;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Structure;
import com.sun.jna.Structure.FieldOrder;

public interface DemoLib extends Library {

    DemoLib INSTANCE = Native.load("demolib", DemoLib.class);

    @FieldOrder({"pos", "vel", "accel"})
    public static class gnc_state_t extends Structure {

        public static class ByValue extends gnc_state_t implements Structure.ByValue {
        }
        public double[] pos = new double[3];
        public double[] vel = new double[3];
        public double[] accel = new double[3];
    }

    @FieldOrder({"quat", "rate", "accel"})
    public static class gnc_attitude_t extends Structure {

        public static class ByValue extends gnc_attitude_t implements Structure.ByValue {
        }
        public double[] quat = new double[4];
        public double[] rate = new double[3];
        public double[] accel = new double[3];
    }

    @FieldOrder({"state", "attitude", "time", "time2", "time3"})
    public static class gnc_pose_t extends Structure {

        public static class ByValue extends gnc_pose_t implements Structure.ByValue {
        }
        public gnc_state_t.ByValue state;
        public gnc_attitude_t.ByValue attitude;
        public double time;
        public double time2;
        public double time3;
    }


    void print_pose(gnc_pose_t.ByValue pose1);
    gnc_pose_t.ByValue return_pose();
}
