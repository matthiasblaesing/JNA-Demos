/* Copyright (c) 2026 Matthias Bläsing, All Rights Reserved
 *
 * The contents of this file is dual-licensed under 2
 * alternative Open Source/Free licenses: LGPL 2.1 or later and
 * Apache License 2.0.
 *
 * You can freely decide which license you want to apply to
 * the project.
 *
 * You may obtain a copy of the LGPL License at:
 *
 * http://www.gnu.org/licenses/licenses.html
 *
 * A copy is also included in the downloadable source code package
 * containing JNA, in file "LGPL2.1".
 *
 * You may obtain a copy of the Apache License at:
 *
 * http://www.apache.org/licenses/
 */
package eu.doppelhelix.dev.jna.libudev;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.PointerType;
import com.sun.jna.platform.linux.Fcntl;
import com.sun.jna.platform.linux.Udev;

import com.sun.jna.platform.linux.Udev.UdevContext;
import com.sun.jna.platform.linux.Udev.UdevDevice;
import eu.doppelhelix.dev.jna.libudev.Main.UdevExt.UdevMonitor;

public class Main {

    interface UdevExt extends Udev {

        @SuppressWarnings("FieldNameHidesFieldInSuperclass")
        public static final UdevExt INSTANCE = Native.load("udev", UdevExt.class);

        public UdevMonitor udev_monitor_ref(UdevMonitor udev_monitor);

        public UdevMonitor udev_monitor_unref(UdevMonitor udev_monitor);

        public UdevContext udev_monitor_get_udev(UdevMonitor udev_monitor);

        public UdevMonitor udev_monitor_new_from_netlink(UdevContext ref, String name);

        public int udev_monitor_get_fd(UdevMonitor ref);

        public UdevDevice udev_monitor_receive_device(UdevMonitor ref);

        public int udev_monitor_filter_add_match_subsystem_devtype(UdevMonitor udev_monitor, String subsystem, String devtype);

        public int udev_monitor_filter_add_match_tag(UdevMonitor udev_monitor, String tag);

        public int udev_monitor_filter_update(UdevMonitor udev_monitor);

        public int udev_monitor_filter_remove(UdevMonitor udev_monitor);

        public UdevListEntry udev_device_get_tags_list_entry(UdevDevice dev);

        public UdevListEntry udev_device_get_current_tags_list_entry(UdevDevice dev);

        public String udev_list_entry_get_value(UdevListEntry dev);

        public static class UdevMonitor extends PointerType {

            public static UdevMonitor newFromNetlink(UdevContext ctx, String name) {
                return INSTANCE.udev_monitor_new_from_netlink(ctx, name);
            }

            public UdevMonitor ref() {
                return INSTANCE.udev_monitor_ref(this);
            }

            public void unref() {
                INSTANCE.udev_monitor_unref(this);
            }

            public UdevContext getUdev() {
                return INSTANCE.udev_monitor_get_udev(this);
            }

            public int getFd() {
                return INSTANCE.udev_monitor_get_fd(this);
            }

            public UdevDevice receiveDevice() {
                return INSTANCE.udev_monitor_receive_device(this);
            }

            public int filterAddMatchSubsystemDevtype(String subsystem, String devtype) {
                return INSTANCE.udev_monitor_filter_add_match_subsystem_devtype(this, subsystem, devtype);
            }
            public int filterAddMatchTag(String tag) {
                return INSTANCE.udev_monitor_filter_add_match_tag(this, tag);
            }
            public int filterRemove() {
                return INSTANCE.udev_monitor_filter_remove(this);
            }
            public int filterUpdate() {
                return INSTANCE.udev_monitor_filter_update(this);
            }
        }
    }

    interface FcntlExt extends Fcntl, Library {

        public static final FcntlExt INSTANCE = Native.load("c", FcntlExt.class);

        public static final int F_DUPFD = 0;
        public static final int F_GETFD = 1;
        public static final int F_SETFD = 2;
        public static final int F_GETFL = 3;
        public static final int F_SETFL = 4;

        int fcntl(int fd, int op);

        int fcntl(int fd, int op, int param);
    }

    public static void main(String[] args) {
        // Create a udev context
        UdevContext udev = UdevExt.INSTANCE.udev_new();
        // Create a monitor, targetting the udev multiplexer as advised by documentation
        UdevMonitor udevMonitor = UdevMonitor.newFromNetlink(udev, "udev");
        if(udevMonitor == null) {
            System.err.println("Failed to open netlink connection for udev");
            return;
        }
        // Switch the udev monitor to blocking mode
        int flags = FcntlExt.INSTANCE.fcntl(udevMonitor.getFd(), FcntlExt.F_GETFL);
        FcntlExt.INSTANCE.fcntl(udevMonitor.getFd(), FcntlExt.F_SETFL, flags & ~FcntlExt.O_NONBLOCK);
        // Add a filter that only selects partition devices in the block subsystem
        System.out.println(udevMonitor.filterAddMatchSubsystemDevtype("block", "partition"));
        System.out.println(udevMonitor.filterUpdate());

        System.out.println("Beginning Monitoring (for partitions)");
        try {
            for (UdevDevice dev = udevMonitor.receiveDevice(); dev != null; dev = udevMonitor.receiveDevice()) {
                System.out.println("----");
                System.out.printf("Syspath:   %s%n", dev.getSyspath());
                System.out.printf("Sysname:   %s%n", dev.getSysname());
                System.out.printf("Subsystem: %s%n", dev.getSubsystem());
                System.out.printf("Devnode:   %s%n", dev.getDevnode());
                System.out.printf("Devtype:   %s%n", dev.getDevtype());
                System.out.println("Tags:");
                for(
                        Udev.UdevListEntry listEntry = UdevExt.INSTANCE.udev_device_get_tags_list_entry(dev);
                        listEntry != null;
                        listEntry = listEntry.getNext()
                ) {
                    System.out.printf("\t%s:%s%n", listEntry.getName(), UdevExt.INSTANCE.udev_list_entry_get_value(listEntry));
                }
                dev.unref();
            }
        } finally {
            udevMonitor.unref();
            udev.unref();
        }

    }
}
