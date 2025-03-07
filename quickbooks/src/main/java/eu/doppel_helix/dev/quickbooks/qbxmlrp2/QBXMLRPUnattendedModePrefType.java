
package eu.doppel_helix.dev.quickbooks.qbxmlrp2;

import com.sun.jna.platform.win32.COM.util.IComEnum;

public enum QBXMLRPUnattendedModePrefType implements IComEnum {
    
    /**
     * (1)
     */
    umpRequired(1),
    
    /**
     * (2)
     */
    umpOptional(2),
    ;

    private QBXMLRPUnattendedModePrefType(long value) {
        this.value = value;
    }
    private long value;

    public long getValue() {
        return this.value;
    }
}