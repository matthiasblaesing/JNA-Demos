
package eu.doppel_helix.dev.blaesing.IDesktopWallpaper;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.COM.COMUtils;
import com.sun.jna.platform.win32.Ole32;
import com.sun.jna.platform.win32.WTypes;
import com.sun.jna.platform.win32.WinDef.RECT;
import com.sun.jna.platform.win32.WinNT.HRESULT;
import com.sun.jna.ptr.PointerByReference;
import eu.doppel_helix.dev.blaesing.IDesktopWallpaper.DesktopWallpaper.DESKTOP_WALLPAPER_POSITION;
import static java.lang.System.out;

public class Main {
    public static void main(String[] args) {
        // COM must be initialized before use
        //
        // Strong suggestions:
        // - ensure there is no other COM initialization happening on this
        //   thread (the Swing EDT uses COM!)
        // - use COINIT_MULTITHREADED if you don't have a _very_ good argument
        //   against it
        HRESULT result = Ole32.INSTANCE.CoInitializeEx(Pointer.NULL, Ole32.COINIT_MULTITHREADED);
        // Check, that initialization succeeded
        COMUtils.checkRC(result);
        try {
            // CoCreateInstance creates the object and returns the pointer to
            // it by placing the pointer into a buffer we allocate
            // (PointerByReference)
            PointerByReference wallpaperBuffer = new PointerByReference();
            result = Ole32.INSTANCE.CoCreateInstance(
                    DesktopWallpaper.CLSID,
                    Pointer.NULL,
                    WTypes.CLSCTX_SERVER,
                    DesktopWallpaper.IID,
                    wallpaperBuffer);
            // Again check that it succeeded
            COMUtils.checkRC(result);
            // Wrap the pointer we got from native with our binding code
            DesktopWallpaper wallpaper = new DesktopWallpaper(wallpaperBuffer.getValue());
            try {
                // Iterate all monitors
                int deviceCount = wallpaper.GetMonitorDevicePathCount();
                for (int i = 0; i < deviceCount; i++) {
                    // Get the device name
                    String deviceName = wallpaper.GetMonitorDevicePathAt(i);
                    // Get the current wallpaper
                    String wallpaperName = wallpaper.GetWallpaper(i);
                    // Get the dimensions of the monitor
                    RECT rect = wallpaper.GetMonitorRect(deviceName);
                    DESKTOP_WALLPAPER_POSITION position = wallpaper.GetPosition();
                    System.out.printf("%40s: bottom: %d/ left: %d/ right: %d/ top: %d%n", deviceName, rect.bottom, rect.left, rect.right, rect.top);
                    System.out.printf("\t%s (%s)%n", wallpaperName, position);
                    // Set a new wallpaper
                    wallpaper.SetWallpaper(i, "c:\\windows\\web\\wallpaper\\theme1\\img13.jpg");
                }
            } finally {
                // COM objects are reference counted, so release the objects
                wallpaper.Release();
            }
        } finally {
            // Uninitialize COM
            Ole32.INSTANCE.CoUninitialize();
        }
    }
}
