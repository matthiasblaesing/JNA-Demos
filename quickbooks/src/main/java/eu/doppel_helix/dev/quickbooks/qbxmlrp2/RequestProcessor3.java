
package eu.doppel_helix.dev.quickbooks.qbxmlrp2;

import com.sun.jna.platform.win32.COM.COMException;
import com.sun.jna.platform.win32.COM.util.IComEventCallbackCookie;
import com.sun.jna.platform.win32.COM.util.IComEventCallbackListener;
import com.sun.jna.platform.win32.COM.util.IConnectionPoint;
import com.sun.jna.platform.win32.COM.util.IUnknown;
import com.sun.jna.platform.win32.COM.util.annotation.ComObject;
import com.sun.jna.platform.win32.COM.util.IRawDispatchHandle;

/**
 * RequestProcessor Class - 3nd version of the library
 *
 * <p>uuid({71F531F5-8E67-4A7A-9161-15733FFC3206})</p>
 * <p>interface(IRequestProcessor5)</p>
 */
@ComObject(clsId = "{71F531F5-8E67-4A7A-9161-15733FFC3206}")
public interface RequestProcessor3 extends IUnknown
    ,IRequestProcessor5
{

}