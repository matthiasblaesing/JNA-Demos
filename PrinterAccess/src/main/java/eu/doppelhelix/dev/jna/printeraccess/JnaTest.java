
package eu.doppelhelix.dev.jna.printeraccess;

import com.sun.jna.Memory;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinBase;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.Winspool;
import java.util.function.Consumer;

public class JnaTest {

  private static final int PRINTER_NOTIFY_OPTIONS_REFRESH = 0x01;

  private static final int JOB_NOTIFY_TYPE = 0x01;

  private static final int TWO_DIMENSIONAL_PRINTERS = 0;

  private static final int NUMBER_OF_BYTES_IN_WORD = 2;

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
      optionsType.Count = 3;
      optionsType.pFields = new Memory(3 * NUMBER_OF_BYTES_IN_WORD);
      optionsType.pFields.write(0,
                                new short[] { Winspool2.PRINTER_NOTIFY_INFO_DATA.JOB_NOTIFY_FIELD_PRINTER_NAME,
                                              Winspool2.PRINTER_NOTIFY_INFO_DATA.JOB_NOTIFY_FIELD_STATUS,
                                              Winspool2.PRINTER_NOTIFY_INFO_DATA.JOB_NOTIFY_FIELD_DOCUMENT
                                },
                                0,
                                3);
      optionsType.toArray(1);
      options.pTypes = optionsType;
      options.write();
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
          Winspool2.PRINTER_NOTIFY_INFO.ByReference info = new Winspool2.PRINTER_NOTIFY_INFO.ByReference();
          success = Winspool.INSTANCE.FindNextPrinterChangeNotification(changeNotificationsHandle,
                                                                        change,
                                                                        optionsPointer,
                                                                        new WinDef.LPVOID(info.getPointer()));
          if (!success) {
            int errorCode = Kernel32.INSTANCE.GetLastError();
            throw new RuntimeException("Failed to get printer change notification - " + errorCode);
          }

          info.read();
          System.out.println("Change - " + String.format("0x%08X", change.getValue().longValue()));
          System.out.println(info.Version); // This should be printing 2, but I get a large number instead
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