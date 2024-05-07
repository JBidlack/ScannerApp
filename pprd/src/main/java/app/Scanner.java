package app;

import javax.usb.UsbDevice;
import javax.usb.UsbException;
import javax.usb.UsbHostManager;
import javax.usb.UsbHub;
import javax.usb.UsbServices;

public class Scanner {
    public Scanner(){
        try {
            UsbServices service = UsbHostManager.getUsbServices();

            UsbHub root = service.getRootUsbHub();

            UsbDevice scanner = findDevice(root);

        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UsbException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }

    private static UsbDevice findDevice(UsbHub root){

        return null;
    }
    
    
}
