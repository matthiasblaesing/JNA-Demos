
package eu.doppel_helix.dev.quickbooks.qbxmlrp2;

import com.sun.jna.platform.win32.COM.util.annotation.ComInterface;
import com.sun.jna.platform.win32.COM.util.annotation.ComMethod;
import com.sun.jna.platform.win32.COM.util.annotation.ComProperty;
import com.sun.jna.platform.win32.COM.util.IDispatch;
import com.sun.jna.platform.win32.COM.util.IUnknown;
import com.sun.jna.platform.win32.COM.util.IRawDispatchHandle;
import com.sun.jna.platform.win32.Variant.VARIANT;

/**
 * IAuthPreferences2 Interface
 *
 * <p>uuid({F810CC1A-151E-48A8-84C9-3F6978FB699C})</p>
 */
@ComInterface(iid="{F810CC1A-151E-48A8-84C9-3F6978FB699C}")
public interface IAuthPreferences2 extends IUnknown, IRawDispatchHandle, IDispatch {
    /**
     * method WasAuthPreferencesObeyed
     *
     * <p>id(0x1)</p>
     * <p>vtableId(7)</p>
     * @param ticket [in] {@code String}
     */
    @ComMethod(name = "WasAuthPreferencesObeyed", dispId = 0x1)
    Boolean WasAuthPreferencesObeyed(String ticket);
            
    /**
     * method GetIsReadOnly
     *
     * <p>id(0x2)</p>
     * <p>vtableId(8)</p>
     * @param ticket [in] {@code String}
     */
    @ComMethod(name = "GetIsReadOnly", dispId = 0x2)
    Boolean GetIsReadOnly(String ticket);
            
    /**
     * method PutIsReadOnly
     *
     * <p>id(0x3)</p>
     * <p>vtableId(9)</p>
     * @param isReadOnly [in] {@code Boolean}
     */
    @ComMethod(name = "PutIsReadOnly", dispId = 0x3)
    void PutIsReadOnly(Boolean isReadOnly);
            
    /**
     * method GetUnattendedModePref
     *
     * <p>id(0x4)</p>
     * <p>vtableId(10)</p>
     * @param ticket [in] {@code String}
     */
    @ComMethod(name = "GetUnattendedModePref", dispId = 0x4)
    QBXMLRPUnattendedModePrefType GetUnattendedModePref(String ticket);
            
    /**
     * method PutUnattendedModePref
     *
     * <p>id(0x5)</p>
     * <p>vtableId(11)</p>
     * @param unattendedModePref [in] {@code QBXMLRPUnattendedModePrefType}
     */
    @ComMethod(name = "PutUnattendedModePref", dispId = 0x5)
    void PutUnattendedModePref(QBXMLRPUnattendedModePrefType unattendedModePref);
            
    /**
     * method GetPersonalDataPref
     *
     * <p>id(0x6)</p>
     * <p>vtableId(12)</p>
     * @param ticket [in] {@code String}
     */
    @ComMethod(name = "GetPersonalDataPref", dispId = 0x6)
    QBXMLRPPersonalDataPrefType GetPersonalDataPref(String ticket);
            
    /**
     * method PutPersonalDataPref
     *
     * <p>id(0x7)</p>
     * <p>vtableId(13)</p>
     * @param personalDataPref [in] {@code QBXMLRPPersonalDataPrefType}
     */
    @ComMethod(name = "PutPersonalDataPref", dispId = 0x7)
    void PutPersonalDataPref(QBXMLRPPersonalDataPrefType personalDataPref);
            
    /**
     * method PutAuthFlags
     *
     * <p>id(0x8)</p>
     * <p>vtableId(14)</p>
     * @param authFlags [in] {@code Integer}
     */
    @ComMethod(name = "PutAuthFlags", dispId = 0x8)
    void PutAuthFlags(Integer authFlags);
            
    
}