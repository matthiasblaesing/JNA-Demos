
package eu.doppel_helix.dev.quickbooks.qbxmlrp2;

import com.sun.jna.platform.win32.COM.util.IComEnum;

public enum QBXMLRPConnectionType implements IComEnum {
    
    /**
     * (0)
     */
    unknown(0),
    
    /**
     * (1)
     */
    localQBD(1),
    
    /**
     * (2)
     */
    remoteQBD(2),
    
    /**
     * (3)
     */
    localQBDLaunchUI(3),
    
    /**
     * (4)
     */
    remoteQBOE(4),
    ;

    private QBXMLRPConnectionType(long value) {
        this.value = value;
    }
    private long value;

    public long getValue() {
        return this.value;
    }
}