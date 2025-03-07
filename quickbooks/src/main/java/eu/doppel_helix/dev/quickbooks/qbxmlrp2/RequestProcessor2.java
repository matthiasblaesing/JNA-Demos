
package eu.doppel_helix.dev.quickbooks.qbxmlrp2;

import com.sun.jna.platform.win32.COM.COMException;
import com.sun.jna.platform.win32.COM.util.IComEventCallbackCookie;
import com.sun.jna.platform.win32.COM.util.IComEventCallbackListener;
import com.sun.jna.platform.win32.COM.util.IConnectionPoint;
import com.sun.jna.platform.win32.COM.util.IUnknown;
import com.sun.jna.platform.win32.COM.util.annotation.ComObject;
import com.sun.jna.platform.win32.COM.util.IRawDispatchHandle;

/**
 * RequestProcessor Class - 2nd version of the library
 *
 * <p>uuid({45F5708E-3B43-4FA8-BE7E-A5F1849214CB})</p>
 * <p>interface(IRequestProcessor4)</p>
 */
@ComObject(clsId = "{45F5708E-3B43-4FA8-BE7E-A5F1849214CB}")
public interface RequestProcessor2 extends IUnknown
    ,IRequestProcessor4
{

}