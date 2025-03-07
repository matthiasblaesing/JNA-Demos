
package eu.doppel_helix.dev.quickbooks.qbxmlrp2;

import com.sun.jna.platform.win32.COM.util.IComEnum;

/**
 * enum QBFileMode
 *
 * <p>uuid({1BF2FE55-3BE7-44F7-AEC4-157814B73ADB})</p>
 */
public enum QBFileMode implements IComEnum {
    
    /**
     * (0)
     */
    qbFileOpenSingleUser(0),
    
    /**
     * (1)
     */
    qbFileOpenMultiUser(1),
    
    /**
     * (2)
     */
    qbFileOpenDoNotCare(2),
    ;

    private QBFileMode(long value) {
        this.value = value;
    }
    private long value;

    public long getValue() {
        return this.value;
    }
}