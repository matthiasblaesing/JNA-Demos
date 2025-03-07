
package eu.doppel_helix.dev.quickbooks.qbxmlrp2;

import com.sun.jna.platform.win32.COM.util.annotation.ComInterface;
import com.sun.jna.platform.win32.COM.util.annotation.ComMethod;
import com.sun.jna.platform.win32.COM.util.annotation.ComProperty;
import com.sun.jna.platform.win32.COM.util.IDispatch;
import com.sun.jna.platform.win32.COM.util.IUnknown;
import com.sun.jna.platform.win32.COM.util.IRawDispatchHandle;
import com.sun.jna.platform.win32.Variant.VARIANT;

/**
 * IRequestProcessor5 Interface
 *
 * <p>uuid({9F44A4C6-B26E-4277-B514-F776243FFC01})</p>
 */
@ComInterface(iid="{9F44A4C6-B26E-4277-B514-F776243FFC01}")
public interface IRequestProcessor5 extends IUnknown, IRawDispatchHandle, IDispatch {
    /**
     * method ProcessRequest
     *
     * <p>id(0x1)</p>
     * <p>vtableId(7)</p>
     * @param ticket [in] {@code String}
     * @param inputRequest [in] {@code String}
     */
    @ComMethod(name = "ProcessRequest", dispId = 0x1)
    String ProcessRequest(String ticket,
            String inputRequest);
            
    /**
     * method OpenConnection
     *
     * <p>id(0x2)</p>
     * <p>vtableId(8)</p>
     * @param appID [in] {@code String}
     * @param appName [in] {@code String}
     */
    @ComMethod(name = "OpenConnection", dispId = 0x2)
    void OpenConnection(String appID,
            String appName);
            
    /**
     * method CloseConnection
     *
     * <p>id(0x3)</p>
     * <p>vtableId(9)</p>
     */
    @ComMethod(name = "CloseConnection", dispId = 0x3)
    void CloseConnection();
            
    /**
     * method BeginSession
     *
     * <p>id(0x4)</p>
     * <p>vtableId(10)</p>
     * @param qbFileName [in] {@code String}
     * @param reqFileMode [in] {@code QBFileMode}
     */
    @ComMethod(name = "BeginSession", dispId = 0x4)
    String BeginSession(String qbFileName,
            QBFileMode reqFileMode);
            
    /**
     * method EndSession
     *
     * <p>id(0x5)</p>
     * <p>vtableId(11)</p>
     * @param ticket [in] {@code String}
     */
    @ComMethod(name = "EndSession", dispId = 0x5)
    void EndSession(String ticket);
            
    /**
     * method GetCurrentCompanyFileName
     *
     * <p>id(0x6)</p>
     * <p>vtableId(12)</p>
     * @param ticket [in] {@code String}
     */
    @ComMethod(name = "GetCurrentCompanyFileName", dispId = 0x6)
    String GetCurrentCompanyFileName(String ticket);
            
    /**
     * property MajorVersion
     *
     * <p>id(0x7)</p>
     * <p>vtableId(13)</p>
     */
    @ComProperty(name = "MajorVersion", dispId = 0x7)
    Short getMajorVersion();
            
    /**
     * property MinorVersion
     *
     * <p>id(0x8)</p>
     * <p>vtableId(14)</p>
     */
    @ComProperty(name = "MinorVersion", dispId = 0x8)
    Short getMinorVersion();
            
    /**
     * property ReleaseLevel
     *
     * <p>id(0x9)</p>
     * <p>vtableId(15)</p>
     */
    @ComProperty(name = "ReleaseLevel", dispId = 0x9)
    QBXMLRPReleaseLevel getReleaseLevel();
            
    /**
     * property ReleaseNumber
     *
     * <p>id(0xa)</p>
     * <p>vtableId(16)</p>
     */
    @ComProperty(name = "ReleaseNumber", dispId = 0xa)
    Short getReleaseNumber();
            
    /**
     * property QBXMLVersionsForSession
     *
     * <p>id(0xb)</p>
     * <p>vtableId(17)</p>
     * @param ticket [in] {@code String}
     */
    @ComProperty(name = "QBXMLVersionsForSession", dispId = 0xb)
    String getQBXMLVersionsForSession(String ticket);
            
    /**
     * property ConnectionType
     *
     * <p>id(0xc)</p>
     * <p>vtableId(18)</p>
     */
    @ComProperty(name = "ConnectionType", dispId = 0xc)
    QBXMLRPConnectionType getConnectionType();
            
    /**
     * method ProcessSubscription
     *
     * <p>id(0xd)</p>
     * <p>vtableId(19)</p>
     * @param inputRequest [in] {@code String}
     */
    @ComMethod(name = "ProcessSubscription", dispId = 0xd)
    String ProcessSubscription(String inputRequest);
            
    /**
     * property QBXMLVersionsForSubscription
     *
     * <p>id(0xe)</p>
     * <p>vtableId(20)</p>
     */
    @ComProperty(name = "QBXMLVersionsForSubscription", dispId = 0xe)
    String getQBXMLVersionsForSubscription();
            
    /**
     * method OpenConnection2
     *
     * <p>id(0xf)</p>
     * <p>vtableId(21)</p>
     * @param appID [in] {@code String}
     * @param appName [in] {@code String}
     * @param connPref [in] {@code QBXMLRPConnectionType}
     */
    @ComMethod(name = "OpenConnection2", dispId = 0xf)
    void OpenConnection2(String appID,
            String appName,
            QBXMLRPConnectionType connPref);
            
    /**
     * property AuthPreferences
     *
     * <p>id(0x10)</p>
     * <p>vtableId(22)</p>
     */
    @ComProperty(name = "AuthPreferences", dispId = 0x10)
    IAuthPreferences getAuthPreferences();
            
    /**
     * method GetQBLastError
     *
     * <p>id(0x11)</p>
     * <p>vtableId(23)</p>
     */
    @ComMethod(name = "GetQBLastError", dispId = 0x11)
    String GetQBLastError();
            
    
}