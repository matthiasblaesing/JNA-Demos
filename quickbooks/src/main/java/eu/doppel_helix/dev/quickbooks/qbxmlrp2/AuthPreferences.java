
package eu.doppel_helix.dev.quickbooks.qbxmlrp2;

import com.sun.jna.platform.win32.COM.COMException;
import com.sun.jna.platform.win32.COM.util.IComEventCallbackCookie;
import com.sun.jna.platform.win32.COM.util.IComEventCallbackListener;
import com.sun.jna.platform.win32.COM.util.IConnectionPoint;
import com.sun.jna.platform.win32.COM.util.IUnknown;
import com.sun.jna.platform.win32.COM.util.annotation.ComObject;
import com.sun.jna.platform.win32.COM.util.IRawDispatchHandle;

/**
 * AuthPreferences Class
 *
 * <p>uuid({3A61350B-7F27-46C4-847E-1089C4C3321B})</p>
 * <p>interface(IAuthPreferences2)</p>
 */
@ComObject(clsId = "{3A61350B-7F27-46C4-847E-1089C4C3321B}")
public interface AuthPreferences extends IUnknown
    ,IAuthPreferences2
{

}