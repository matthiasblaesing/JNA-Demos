
package eu.doppel_helix.dev.quickbooks.qbxmlrp2;

import com.sun.jna.platform.win32.COM.util.IComEnum;

public enum QBXMLRPReleaseLevel implements IComEnum {
    
    /**
     * (0)
     */
    preAlpha(0),
    
    /**
     * (1)
     */
    alpha(1),
    
    /**
     * (2)
     */
    beta(2),
    
    /**
     * (3)
     */
    release(3),
    ;

    private QBXMLRPReleaseLevel(long value) {
        this.value = value;
    }
    private long value;

    public long getValue() {
        return this.value;
    }
}