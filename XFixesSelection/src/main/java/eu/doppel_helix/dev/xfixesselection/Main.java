/*
 * Copyright 2022 Matthias Bläsing
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package eu.doppel_helix.dev.xfixesselection;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.platform.unix.X11;
import com.sun.jna.platform.unix.X11.Atom;
import com.sun.jna.platform.unix.X11.AtomByReference;
import com.sun.jna.platform.unix.X11.Display;
import com.sun.jna.platform.unix.X11.Window;
import com.sun.jna.platform.unix.X11.XEvent;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.NativeLongByReference;
import com.sun.jna.ptr.PointerByReference;

import static com.sun.jna.platform.unix.X11.XA_PRIMARY;

/**
 * This sample demonstrates access to PRIMARY and CLIPBOARD selection on X11
 *
 * Inspired/Converted from:
 *
 * - https://www.uninformativ.de/blog/postings/2017-04-02/0/POSTING-en.html
 * - https://stackoverflow.com/questions/27378318/c-get-string-from-clipboard-on-linux/44992938#44992938
 */
public class Main {

    public static void main(String[] args) {

        Display display = X11.INSTANCE.XOpenDisplay(null);

        // Check if XFixes extension is present and if so extract the event
        // base address
        IntByReference eventBaseReturnBuffer = new IntByReference();
        IntByReference errorBaseReturnBuffer = new IntByReference();

        if (XFixes.INSTANCE.XFixesQueryExtension(display, eventBaseReturnBuffer, errorBaseReturnBuffer) == 0) {
            throw new RuntimeException("XFixes extension missing");
        }

        int eventBaseReturn = eventBaseReturnBuffer.getValue();
        int errorBaseReturn = errorBaseReturnBuffer.getValue();

        // Start listening for selection events of CLIPBOARD and PRIMARY selection
        Atom clip = X11.INSTANCE.XInternAtom(display, "CLIPBOARD", false);
        Window root = X11.INSTANCE.XDefaultRootWindow(display);

        XFixes.INSTANCE.XFixesSelectSelectionInput(display, root, XA_PRIMARY, new NativeLong(XFixes.XFixesSetSelectionOwnerNotifyMask));
        XFixes.INSTANCE.XFixesSelectSelectionInput(display, root, clip, new NativeLong(XFixes.XFixesSetSelectionOwnerNotifyMask));

        // Run a loop and query X11 events - needs to be kill to end
        XEvent xevent = new XEvent();
        while (true) {
            X11.INSTANCE.XNextEvent(display, xevent);
            // We got a selection notification
            if (xevent.type == (eventBaseReturn + XFixes.XFixesSelectionNotify)) {
                // Convert the basic event into an XFixesSelectionNotifyEvent,
                XFixes.XFixesSelectionNotifyEvent selectionNotify = Structure.newInstance(XFixes.XFixesSelectionNotifyEvent.class, xevent.getPointer());
                selectionNotify.read();
                // We got a selection so we'll try to extract it
                printSelection(display, selectionNotify.owner, selectionNotify.selection);
            }
        }
    }

    private static void printSelection(Display display, Window window, Atom selection) {
        // We need a window to receive the value, the window is placed outside
        // the visible area as it can't be of size 0
        Window targetWindow = X11.INSTANCE.XCreateSimpleWindow(display, X11.INSTANCE.XDefaultRootWindow(display), -10, -10, 1, 1, 0, 0, 0);
        try {
            // targetProperty holds the name of the property the data will be
            // placed in. The property is located on the window we created as
            // targetWindow
            Atom targetProperty = X11.INSTANCE.XInternAtom(display, "SELECTION_TARGET", false);
            // For large transfers the INCR protocol is used - this atom indicates that
            Atom incrMarker = X11.INSTANCE.XInternAtom(display, "INCR", false);
            // Request UTF-8 string
            Atom utf8String = X11.INSTANCE.XInternAtom(display, "UTF8_STRING", false);
            // Request native string
            Atom string = X11.INSTANCE.XInternAtom(display, "STRING", false);

            // Check our known variants
            for (Atom variant : new Atom[]{ utf8String, string }) {
                // Request conversion of the selection to our supported format
                X11Ext.INSTANCE.XConvertSelection(display, selection, variant, targetProperty, targetWindow, new NativeLong(X11.CurrentTime));

                // Wait for the selection to be prepared for us
                XEvent xevent = new XEvent();
                while (true) {
                    X11.INSTANCE.XNextEvent(display, xevent);
                    if (xevent.type == X11.SelectionNotify) {
                        xevent.setType("xselection");
                        xevent.read();
                        if (xevent.xselection.selection.equals(selection)) {
                            break;
                        }
                    }
                }

                // if property is null, conversion failed
                if(xevent.xselection.property == null) {
                    continue;
                }

                AtomByReference actualTypeReturn = new AtomByReference();
                IntByReference actionFormatReturn = new IntByReference();
                NativeLongByReference nItemsReturn = new NativeLongByReference();
                NativeLongByReference bytesAfterReturn = new NativeLongByReference();
                PointerByReference propReturn = new PointerByReference();

                // Fetch type from selection and check it
                X11.INSTANCE.XGetWindowProperty(display,
                        targetWindow,
                        targetProperty,
                        NATIVE_LONG_0,
                        NATIVE_LONG_0,
                        false,
                        ATOM_ANY_PROPERTY_TYPE,
                        actualTypeReturn,
                        actionFormatReturn,
                        nItemsReturn,
                        bytesAfterReturn,
                        propReturn);

                // Free the read data
                X11.INSTANCE.XFree(propReturn.getValue());

                if(actualTypeReturn.getValue().equals(incrMarker)) {
                    System.out.println("INCR protocol is not supported");
                    continue;
                }

                System.out.printf("Selection contains %2$d bytes in %1$s in %3$s encoding%n",
                        selection.equals(XA_PRIMARY) ? "PRIMARY" : "CLIPBOARD",
                        bytesAfterReturn.getValue().intValue(),
                        utf8String.equals(variant) ? "UTF-8" : "native"
                );

                // Do the actual reading in one go
                X11.INSTANCE.XGetWindowProperty(display,
                        targetWindow,
                        targetProperty,
                        NATIVE_LONG_0,
                        new NativeLong(bytesAfterReturn.getValue().intValue()),
                        false,
                        ATOM_ANY_PROPERTY_TYPE,
                        actualTypeReturn,
                        actionFormatReturn,
                        nItemsReturn,
                        bytesAfterReturn,
                        propReturn);

                System.out.println("------------------------------------");
                if(variant.equals(utf8String)) {
                    System.out.println(propReturn.getValue().getString(0, "UTF-8"));
                } else {
                    System.out.println(propReturn.getValue().getString(0));
                }
                System.out.println("------------------------------------");

                // Free the read data
                X11.INSTANCE.XFree(propReturn.getValue());

                // Clear the property
                X11.INSTANCE.XDeleteProperty(display, window, targetProperty);

                break;
            }
        } finally {
            // Remove the temporary window
            X11.INSTANCE.XDestroyWindow(display, targetWindow);
        }
    }

    private static final NativeLong NATIVE_LONG_0 = new NativeLong(0);
    private static final Atom ATOM_ANY_PROPERTY_TYPE = new Atom(X11.AnyPropertyType);

    /**
     * Extend the existing X11 bindings with the currently missing method
     * XConvertSelection.
     */
    public interface X11Ext extends X11 {
        public X11Ext INSTANCE = Native.load("X11", X11Ext.class);

        public int XConvertSelection(Display display, Atom selection, Atom target, Atom property, Window requestor, NativeLong time);
    }

    /**
     * Bindings for the XFixes extension.
     */
    public interface XFixes extends Library {

        XFixes INSTANCE = Native.load("Xfixes", XFixes.class);

        public Window XFixesSelectSelectionInput(Display display, Window rootWindow, Atom selectionBuffer, NativeLong XfixesSelector);

        public int XFixesQueryExtension(Display display, IntByReference event_base_return, IntByReference error_base_return);

        public static final int XFixesSelectionNotify = 0;

        public static final long XFixesSetSelectionOwnerNotifyMask = 1 << 0;
        public static final long XFixesSelectionWindowDestroyNotifyMask = 1 << 1;
        public static final long XFixesSelectionClientCloseNotifyMask = 1 << 2;

        @Structure.FieldOrder({"type", "serial", "send_event", "display", "window", "subtype", "owner", "selection", "timestamp", "selectionTimestamp"})
        class XFixesSelectionNotifyEvent extends Structure {

            public XFixesSelectionNotifyEvent(Pointer p) {
                super(p);
            }

            public int type;
            public NativeLong serial;   // # of last request processed by server
            public int send_event;      // true if this came from a SendEvent request
            public Display display;     // Display the event was read from
            public Window window;
            public int subtype;
            public Window owner;
            public Atom selection;
            public NativeLong timestamp;
            public NativeLong selectionTimestamp;
        }
    }
}
