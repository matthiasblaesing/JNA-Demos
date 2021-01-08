
package eu.doppelhelix.dev.jna.printeraccess;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Union;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;

public interface Winspool2  extends StdCallLibrary {

  Winspool2 INSTANCE = Native.load("Winspool.drv", Winspool2.class, W32APIOptions.DEFAULT_OPTIONS);

  @Structure.FieldOrder({ "Version", "Flags", "Count", "pTypes" })
  public static class PRINTER_NOTIFY_OPTIONS extends Structure {

    public static class ByReference extends PRINTER_NOTIFY_OPTIONS implements Structure.ByReference {
    }

    public int Version;

    public int Flags;

    public int Count;

    public PRINTER_NOTIFY_OPTIONS_TYPE.ByReference pTypes;

  }

  @Structure.FieldOrder({ "Type", "Reserved0", "Reserved1", "Reserved2", "Count", "pFields" })
  public static class PRINTER_NOTIFY_OPTIONS_TYPE extends Structure {

    public static class ByReference extends PRINTER_NOTIFY_OPTIONS_TYPE implements Structure.ByReference {
    }

    public short Type;

    public short Reserved0;

    public int Reserved1;

    public int Reserved2;

    public int Count;

    public Pointer pFields;

    public void setFields(short[] fields) {
        Memory fieldsMemory = new Memory(fields.length * 2 /* size of short in bytes*/);
        fieldsMemory.write(0, fields, 0, fields.length);
        pFields = fieldsMemory;
        Count = fields.length;
    }

    public short[] getFields() {
        return pFields.getShortArray(0, Count);
    }
  }

  @Structure.FieldOrder({ "Version", "Flags", "Count", "aData" })
  public static class PRINTER_NOTIFY_INFO extends Structure {

    public static class ByReference extends PRINTER_NOTIFY_INFO implements Structure.ByReference {
    }

    public static class ByValue extends PRINTER_NOTIFY_INFO implements Structure.ByValue {
    }

    public int Version;

    public int Flags;

    public int Count;

    public PRINTER_NOTIFY_INFO_DATA[] aData = new PRINTER_NOTIFY_INFO_DATA[1];

    @Override
    public void read() {
        // This is a gross hack - zero sized arrays are a problem for the JNA
        // API, so it is special cased here
        int count = (int) readField("Count");
        aData = new PRINTER_NOTIFY_INFO_DATA[count];
        if(count == 0) {
            Count = count;
            Version = (int) readField("Version");
            Flags = (int) readField("Flags");
        } else {
            super.read();
        }
    }

  }

  @Structure.FieldOrder({ "cbBuf", "pBuf" })
  public static class Data_struct extends Structure {

    public static class ByReference extends Data_struct implements Structure.ByReference {
    }

    public static class ByValue extends Data_struct implements Structure.ByValue {
    }

    public int cbBuf;

    public WinDef.PVOID pBuf;

  }

  public static class NotifyData_union extends Union {

    public static class ByReference extends NotifyData_union implements Structure.ByReference {
    }

    public static class ByValue extends NotifyData_union implements Structure.ByValue {
    }

    public int[] adwData = new int[2];

    public Data_struct Data;

  }

  @Structure.FieldOrder({ "Type", "Field", "Reserved", "Id", "NotifyData" })
  public static class PRINTER_NOTIFY_INFO_DATA extends Structure {

    public static final short JOB_NOTIFY_FIELD_PRINTER_NAME = 0x00;

    public static final short JOB_NOTIFY_FIELD_STATUS = 0x0A;

    public static final short JOB_NOTIFY_FIELD_DOCUMENT = 0x0D;

    public static class ByReference extends PRINTER_NOTIFY_INFO_DATA implements Structure.ByReference {
    }

    public static class ByValue extends PRINTER_NOTIFY_INFO_DATA implements Structure.ByValue {
    }

    public short Type;

    public short Field;

    public int Reserved;

    public int Id;

    public NotifyData_union NotifyData;

    @Override
    public void read() {
      super.read();

      if (Type == JOB_NOTIFY_FIELD_STATUS) {
        NotifyData.setType(int[].class);
      } else {
        NotifyData.setType(Data_struct.class);
      }
      NotifyData.read();
    }
  }

  // See https://docs.microsoft.com/en-us/windows/win32/printdocs/freeprinternotifyinfo
  public boolean FreePrinterNotifyInfo(Pointer pni);
}