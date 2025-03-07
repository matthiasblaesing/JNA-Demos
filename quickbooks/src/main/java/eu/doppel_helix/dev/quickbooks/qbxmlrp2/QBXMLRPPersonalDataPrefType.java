
package eu.doppel_helix.dev.quickbooks.qbxmlrp2;

import com.sun.jna.platform.win32.COM.util.IComEnum;

public enum QBXMLRPPersonalDataPrefType implements IComEnum {
    
    /**
     * (1)
     */
    pdpRequired(1),
    
    /**
     * (2)
     */
    pdpOptional(2),
    
    /**
     * (3)
     */
    pdpNotNeeded(3),
    ;

    private QBXMLRPPersonalDataPrefType(long value) {
        this.value = value;
    }
    private long value;

    public long getValue() {
        return this.value;
    }
}