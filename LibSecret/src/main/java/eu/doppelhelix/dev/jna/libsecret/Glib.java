/* Copyright (c) 2021 Matthias Bläsing, All Rights Reserved
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

package eu.doppelhelix.dev.jna.libsecret;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;

public interface Glib extends Library {
    Glib INSTANCE = Native.load("glib-2.0", Glib.class);

    public void g_error_free(Pointer gerror);

    @Structure.FieldOrder({"domain", "code", "message"})
    class GError extends Structure {
	public int domain;
	public int code;
	public String message;
    }
}
