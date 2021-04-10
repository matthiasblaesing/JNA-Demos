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
import com.sun.jna.ptr.PointerByReference;
import eu.doppelhelix.dev.jna.libsecret.Gio.GCancelable;

public interface LibSecret extends Library {
    LibSecret INSTANCE = Native.load("secret-1", LibSecret.class);

    Pointer secret_password_lookup_sync(SecretSchema schema, GCancelable cancellable, PointerByReference gerror, Object... attributes);
    Pointer secret_password_store_sync(SecretSchema schema, String collection, String label, String password, GCancelable cancelable, PointerByReference gerror, Object... attributes);
    Pointer secret_password_clear_sync(SecretSchema schema, GCancelable cancellable, PointerByReference gerror, Object... attributes);
    void secret_password_free(Pointer pointer);

    @Structure.FieldOrder({"name", "flags", "attributes", "reserved", "reserved1", "reserved2", "reserved3", "reserved4", "reserved5", "reserved6", "reserved7"})
    class SecretSchema extends Structure {
	public String name;
	public int flags;
	public SecretSchemaAttribute[] attributes = new SecretSchemaAttribute[32];
	public int reserved;
	public Pointer reserved1;
	public Pointer reserved2;
	public Pointer reserved3;
	public Pointer reserved4;
	public Pointer reserved5;
	public Pointer reserved6;
	public Pointer reserved7;
    }

    @Structure.FieldOrder({"name", "type"})
    class SecretSchemaAttribute extends Structure {
	public String name;
	public int type;
    }

    final int SECRET_SCHEMA_ATTRIBUTE_STRING = 0;
    final int SECRET_SCHEMA_ATTRIBUTE_INTEGER = 1;
    final int SECRET_SCHEMA_ATTRIBUTE_BOOLEAN = 2;

    final int SECRET_SCHEMA_NONE = 0;
    final int SECRET_SCHEMA_DONT_MATCH_NAME = 1;

    final String SECRET_COLLECTION_DEFAULT = "default";
    final String SECRET_COLLECTION_SESSION = "session";
}
