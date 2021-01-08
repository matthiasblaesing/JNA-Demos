
package eu.doppelhelix.dev.jna.printeraccess;

import com.sun.jna.Structure;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinBase;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.Winspool;
import com.sun.jna.ptr.PointerByReference;
import eu.doppelhelix.dev.jna.printeraccess.Winspool2.PRINTER_NOTIFY_INFO;
import static eu.doppelhelix.dev.jna.printeraccess.Winspool2.PRINTER_NOTIFY_INFO_DATA.JOB_NOTIFY_FIELD_DOCUMENT;
import static eu.doppelhelix.dev.jna.printeraccess.Winspool2.PRINTER_NOTIFY_INFO_DATA.JOB_NOTIFY_FIELD_PRINTER_NAME;
import static eu.doppelhelix.dev.jna.printeraccess.Winspool2.PRINTER_NOTIFY_INFO_DATA.JOB_NOTIFY_FIELD_STATUS;
import java.util.function.Consumer;

public class JnaTest {

  private static final int PRINTER_NOTIFY_OPTIONS_REFRESH = 0x01;

  private static final int JOB_NOTIFY_TYPE = 0x01;

  private static final int TWO_DIMENSIONAL_PRINTERS = 0;

  public static void main(String[] args) throws Exception {
    WinNT.HANDLEByReference printServerHandle = new WinNT.HANDLEByReference();
    boolean success = Winspool.INSTANCE.OpenPrinter(null, printServerHandle, null); // Get job info for all printers
    if (!success) {
      int errorCode = Kernel32.INSTANCE.GetLastError();
      throw new RuntimeException("Failed to access the print server - " + errorCode);
    }

    try {
      Winspool2.PRINTER_NOTIFY_OPTIONS options = new Winspool2.PRINTER_NOTIFY_OPTIONS();
      options.Version = 2;
      options.Flags = PRINTER_NOTIFY_OPTIONS_REFRESH;
      options.Count = 1;
      Winspool2.PRINTER_NOTIFY_OPTIONS_TYPE.ByReference optionsType = new Winspool2.PRINTER_NOTIFY_OPTIONS_TYPE.ByReference();
      optionsType.Type = JOB_NOTIFY_TYPE;
      // The logic for handling the encoding of the fields was moved to the
      // implementation of the PRINTER_NOTIFY_OPTIONS_TYPE structure
      optionsType.setFields(new short[] { JOB_NOTIFY_FIELD_PRINTER_NAME, JOB_NOTIFY_FIELD_STATUS, JOB_NOTIFY_FIELD_DOCUMENT });
      optionsType.toArray(1);
      options.pTypes = optionsType;
      options.write();
      /*
       * it would have been better to bind the final argument of
       * FindNextPrinterChangeNotification directly as either a Structure
       * type or directly with PRINTER_NOTIFY_TYPE - might be a worthy overload
       */
      WinDef.LPVOID optionsPointer = new WinDef.LPVOID(options.getPointer());
      WinNT.HANDLE changeNotificationsHandle = Winspool.INSTANCE.FindFirstPrinterChangeNotification(printServerHandle.getValue(),
                                                                                                    Winspool.PRINTER_CHANGE_ADD_JOB
                                                                                                    | Winspool.PRINTER_CHANGE_SET_JOB
                                                                                                    | Winspool.PRINTER_CHANGE_DELETE_JOB,
                                                                                                    TWO_DIMENSIONAL_PRINTERS,
                                                                                                    optionsPointer);
      if (!isValidHandle(changeNotificationsHandle)) {
        int errorCode = Kernel32.INSTANCE.GetLastError();
        throw new RuntimeException("Failed to get a change handle - " + errorCode);
      }

      try {
        while (true) {
          Kernel32.INSTANCE.WaitForSingleObject(changeNotificationsHandle, WinBase.INFINITE);

          WinDef.DWORDByReference change = new WinDef.DWORDByReference();

          // At this point a pointer to a structure was passed to
          // FindNextPrinterChangeNotification, but that is invalid. The function
          // requires a pointer sized buffer. That buffer is filled with a
          // pointer to a structure, that holds the actual data. The structure
          // is allocated by the infrastructure and needs to be freed by us.
          //
          // It would have been better to bind the final argument of
          // FindNextPrinterChangeNotification directly as PointerByReference -
          // might be a worthy overload.
          PointerByReference ppPrinterNotifyInfo = new PointerByReference();
          success = Winspool.INSTANCE.FindNextPrinterChangeNotification(changeNotificationsHandle,
                                                                        change,
                                                                        optionsPointer,
                                                                        new WinDef.LPVOID(ppPrinterNotifyInfo.getPointer()));
          if (!success) {
            int errorCode = Kernel32.INSTANCE.GetLastError();
            throw new RuntimeException("Failed to get printer change notification - " + errorCode);
          }

          System.out.println("Change - " + String.format("0x%08X", change.getValue().longValue()));

          // Not sure if this could be null, but let's be prepared
          if (ppPrinterNotifyInfo.getValue() != null) {
                // Extract the buffer pointer from the PointerByReference and
                // wrap it into the structure
                PRINTER_NOTIFY_INFO info = Structure.newInstance(PRINTER_NOTIFY_INFO.class, ppPrinterNotifyInfo.getValue());
                info.read();

                // Default Structure#toString dumps the structures structure
                // well suited to see alignment problems.
                System.out.println(info);
                for (int i = 0; i < info.aData.length; i++) {
                    System.out.println("--------------------------------------------------");
                    System.out.println(info.aData[i]);
                }
                System.out.println("==================================================");
                // Free the PRINTER_NOTIFY_INFO structure that was returned
                // The function is bound with the raw pointer as the info structure
                // can contain a zero-length array.
                Winspool2.INSTANCE.FreePrinterNotifyInfo(info.getPointer());
            }
        }
      } finally {
        runIfValidHandle(changeNotificationsHandle, Winspool.INSTANCE::FindClosePrinterChangeNotification);
      }
    } finally {
      runIfValidHandle(printServerHandle.getValue(), Winspool.INSTANCE::ClosePrinter);
    }
  }

  static boolean isValidHandle(WinNT.HANDLE handle) {
    return handle != null && !handle.equals(Kernel32.INVALID_HANDLE_VALUE);
  }

  static void runIfValidHandle(WinNT.HANDLE handle, Consumer<WinNT.HANDLE> consumer) {
    if (isValidHandle(handle)) {
      consumer.accept(handle);
    }
  }
}