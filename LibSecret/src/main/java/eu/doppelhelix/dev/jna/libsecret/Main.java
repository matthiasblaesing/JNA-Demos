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

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.ptr.PointerByReference;
import eu.doppelhelix.dev.jna.libsecret.Glib.GError;

import static eu.doppelhelix.dev.jna.libsecret.LibSecret.SECRET_COLLECTION_DEFAULT;
import static eu.doppelhelix.dev.jna.libsecret.LibSecret.SECRET_SCHEMA_ATTRIBUTE_STRING;
import static eu.doppelhelix.dev.jna.libsecret.LibSecret.SECRET_SCHEMA_NONE;

import eu.doppelhelix.dev.jna.libsecret.LibSecret.SecretSchema;
import eu.doppelhelix.dev.jna.libsecret.LibSecret.SecretSchemaAttribute;

public class Main {

    public static void main(String[] args) {
        PointerByReference gerrorBuffer = new PointerByReference();

        LibSecret.SecretSchema schema = new SecretSchema();
        schema.name = "dummy";
        schema.flags = SECRET_SCHEMA_NONE;
        schema.attributes[0] = new SecretSchemaAttribute();
        schema.attributes[0].name = "id";
        schema.attributes[0].type = SECRET_SCHEMA_ATTRIBUTE_STRING;

        LibSecret.INSTANCE.secret_password_store_sync(schema, SECRET_COLLECTION_DEFAULT, "Dummy Entry", "Dummy Password - really!", null, gerrorBuffer, "id", "42");

        if (gerrorBuffer.getValue() != null) {
            GError gerror = (GError) Structure.newInstance(GError.class, gerrorBuffer.getValue());
            gerror.read();
            System.out.printf("%d/%d: %s%n", gerror.domain, gerror.code, gerror.message);
            Glib.INSTANCE.g_error_free(gerrorBuffer.getValue());
            System.exit(1);
        }

        Pointer pointer = LibSecret.INSTANCE.secret_password_lookup_sync(schema, null, gerrorBuffer, "id", "42");

        if (gerrorBuffer.getValue() != null) {
            GError gerror = (GError) Structure.newInstance(GError.class, gerrorBuffer.getValue());
            gerror.read();
            System.out.printf("%d/%d: %s%n", gerror.domain, gerror.code, gerror.message);
            Glib.INSTANCE.g_error_free(gerrorBuffer.getValue());
            System.exit(1);
        }

        if (pointer == null) {
            System.out.println("PASSWORD NOT FOUND");
        } else {
            System.out.println("Fetched password: " + pointer.getString(0));
            LibSecret.INSTANCE.secret_password_free(pointer);
        }

        LibSecret.INSTANCE.secret_password_clear_sync(schema, null, gerrorBuffer, "id", "42");

        if (gerrorBuffer.getValue() != null) {
            GError gerror = (GError) Structure.newInstance(GError.class, gerrorBuffer.getValue());
            gerror.read();
            System.out.printf("%d/%d: %s%n", gerror.domain, gerror.code, gerror.message);
            Glib.INSTANCE.g_error_free(gerrorBuffer.getValue());
            System.exit(1);
        }
    }
}
