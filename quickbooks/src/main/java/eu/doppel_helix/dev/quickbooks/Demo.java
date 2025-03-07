
package eu.doppel_helix.dev.quickbooks;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.COM.util.Factory;
import com.sun.jna.platform.win32.Ole32;
import eu.doppel_helix.dev.quickbooks.qbxmlrp2.IRequestProcessor5;
import eu.doppel_helix.dev.quickbooks.qbxmlrp2.QBFileMode;
import eu.doppel_helix.dev.quickbooks.qbxmlrp2.QBXMLRPConnectionType;

public class Demo {
    public static void main(String[] args) {
        Ole32.INSTANCE.CoInitializeEx(Pointer.NULL, Ole32.COINIT_MULTITHREADED);
        try {
            Factory factory = new Factory();
            QBXMLRP2RequestProcessor processorObj = factory.createObject(QBXMLRP2RequestProcessor.class);
            IRequestProcessor5 processor = processorObj.queryInterface(IRequestProcessor5.class);
            processor.OpenConnection2(null, "My Sample App", QBXMLRPConnectionType.localQBD);
            String ticket = processor.BeginSession("", QBFileMode.qbFileOpenDoNotCare);
            String result = processor.ProcessRequest(ticket, "<xml>Test</xml>");
            System.out.println(result);
            processor.EndSession(ticket);
            processor.CloseConnection();
        } finally {
            Ole32.INSTANCE.CoUninitialize();
        }
    }
}
