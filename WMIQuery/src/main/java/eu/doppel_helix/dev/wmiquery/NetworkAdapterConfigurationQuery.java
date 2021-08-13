package eu.doppel_helix.dev.wmiquery;

import com.sun.jna.platform.win32.COM.COMUtils;
import com.sun.jna.platform.win32.COM.Unknown;
import com.sun.jna.platform.win32.COM.Wbemcli;
import com.sun.jna.platform.win32.COM.WbemcliUtil;
import com.sun.jna.platform.win32.OaIdl.SAFEARRAY;
import com.sun.jna.platform.win32.Ole32;
import com.sun.jna.platform.win32.OleAuto;
import com.sun.jna.platform.win32.Variant;
import com.sun.jna.platform.win32.Variant.VARIANT;
import com.sun.jna.ptr.IntByReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NetworkAdapterConfigurationQuery {

    public static void main(String[] args) {
        // Initialize COM subsystem
        Ole32.INSTANCE.CoInitializeEx(null, Ole32.COINIT_MULTITHREADED);

        // Initialize WMI connection
        Wbemcli.IWbemServices svc = WbemcliUtil.connectServer("ROOT\\CIMV2");
        try {
            // Query the WMI
            String[] props = {
                "Caption", "Description", "SettingID", "ArpAlwaysSourceRoute",
                "ArpUseEtherSNAP", "DatabasePath", "DeadGWDetectEnabled",
                "DefaultIPGateway", "DefaultTOS", "DefaultTTL",
                "DHCPEnabled", "DHCPLeaseExpires", "DHCPLeaseObtained",
                "DHCPServer", "DNSDomain", "DNSDomainSuffixSearchOrder",
                "DNSEnabledForWINSResolution", "DNSHostName",
                "DNSServerSearchOrder", "DomainDNSRegistrationEnabled",
                "ForwardBufferMemory", "FullDNSRegistrationEnabled",
                "GatewayCostMetric", "IGMPLevel", "Index", "InterfaceIndex",
                "IPAddress", "IPConnectionMetric", "IPEnabled",
                "IPFilterSecurityEnabled", "IPPortSecurityEnabled",
                "IPSecPermitIPProtocols", "IPSecPermitTCPPorts",
                "IPSecPermitUDPPorts", "IPSubnet", "IPUseZeroBroadcast",
                "IPXAddress", "IPXEnabled", "IPXFrameType", "IPXMediaType",
                "IPXNetworkNumber", "IPXVirtualNetNumber", "KeepAliveInterval",
                "KeepAliveTime", "MACAddress", "MTU", "NumForwardPackets",
                "PMTUBHDetectEnabled", "PMTUDiscoveryEnabled", "ServiceName",
                "TcpipNetbiosOptions", "TcpMaxConnectRetransmissions",
                "TcpMaxDataRetransmissions", "TcpNumConnections",
                "TcpUseRFC1122UrgentPointer", "TcpWindowSize",
                "WINSEnableLMHostsLookup", "WINSHostLookupFile",
                "WINSPrimaryServer", "WINSScopeID", "WINSSecondaryServer"};

            Wbemcli.IEnumWbemClassObject enumerator = svc.ExecQuery("WQL",
                "SELECT " + String.join(",", props) + " FROM Win32_NetworkAdapterConfiguration",
                Wbemcli.WBEM_FLAG_FORWARD_ONLY | Wbemcli.WBEM_FLAG_RETURN_IMMEDIATELY, null);

            try {
                Wbemcli.IWbemClassObject[] result;

                // Enumerate the results of the Query
                while ((result = enumerator.Next(1000, 1)).length > 0) {
                    System.out.println("-------------RESULT_ENTRY------------");
                    for (String prop : props) {
                        System.out.print(prop + "> ");
                        IntByReference pType = new IntByReference();
                        IntByReference plFlavor = new IntByReference();
                        // Each "Get" call needs its own(!) VARIANT#ByReference
                        Variant.VARIANT.ByReference pVal = new Variant.VARIANT.ByReference();
                        COMUtils.checkRC(result[0].Get(prop, 0, pVal, pType, plFlavor));
                        // Special case the SAFEARRAY case and extract the
                        // array values in case it is a one dimensional array
                        if (pVal.getValue() instanceof SAFEARRAY) {
                            SAFEARRAY sa = (SAFEARRAY) pVal.getValue();
                            int dimensions = sa.getDimensionCount();
                            if (dimensions == 1) {
                                int lBound = sa.getLBound(0);
                                int uBound = sa.getUBound(0);
                                List<String> data = new ArrayList<>();
                                for (int i = lBound; i <= uBound; i++) {
                                    Object element = sa.getElement(i);
                                    data.add(String.valueOf(element));
                                    // Free Values if necessary
                                    if(element instanceof VARIANT) {
                                        OleAuto.INSTANCE.VariantClear(((VARIANT) element));
                                    } else if (element instanceof Unknown) {
                                        ((Unknown) element).Release();
                                    }
                                }
                                System.out.println(data);
                            } else {
                                // For multi dimensional arrays only output the dimensions
                                String[] bounds = new String[dimensions];
                                for (int dim = 0; dim < dimensions; dim++) {
                                    bounds[dim] = sa.getLBound(dim) + "-" + sa.getUBound(dim);
                                }
                                System.out.printf("MultiDim[%s]", Arrays.toString(bounds));
                            }
                        } else {
                            // For skalars output the value and the type
                            System.out.println(pVal.getValue() + " {Type: " + pVal.getVarType() + "}");
                        }
                        // Clear the variant initialized by IWbemClassObject#Get
                        OleAuto.INSTANCE.VariantClear(pVal);
                    }
                    // Release the result
                    result[0].Release();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                enumerator.Release();
            }
        } finally {
            // Release reference to COM object and shutdown COM subsystem
            svc.Release();
            Ole32.INSTANCE.CoUninitialize();
        }
    }
}
