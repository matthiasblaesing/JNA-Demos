package eu.doppel_helix.dev.quickbooks;

import com.sun.jna.platform.win32.COM.util.IUnknown;
import com.sun.jna.platform.win32.COM.util.annotation.ComObject;

@ComObject(progId = "QBXMLRP2.RequestProcessor")
public interface QBXMLRP2RequestProcessor extends IUnknown {

}
