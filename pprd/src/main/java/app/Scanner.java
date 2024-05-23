package app;

import java.util.List;

import javax.usb.UsbDevice;
import javax.usb.UsbDeviceDescriptor;
import javax.usb.UsbException;
import javax.usb.UsbHostManager;
import javax.usb.UsbHub;
import javax.usb.UsbServices;

public class Scanner {
    private static final short VID = 0x18D1;
    private static final short PID = 0x4EE7;
    protected static UsbDevice scanner = null;
    private static UsbHub root = null;

    public Scanner(){
        try {
            UsbServices service = UsbHostManager.getUsbServices();

            root = service.getRootUsbHub();

            scanner = findDevice(root);

            // scanner.addUsbDeviceListener(l -> );

        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UsbException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    public UsbDevice retryFind(){

        findDevice(root);
        return null;
    }

    @SuppressWarnings("unchecked")
    private static UsbDevice findDevice(UsbHub hub){
        //UsbDevice scanner = null;

        for (UsbDevice device: (List<UsbDevice>) hub.getAttachedUsbDevices()){
            if(device.isUsbHub()){
                scanner = findDevice((UsbHub) device);
                if(scanner != null){
                    return scanner;
                }
            }
            else{
                UsbDeviceDescriptor desc = device.getUsbDeviceDescriptor();

                if(desc.idVendor() == VID && desc.idProduct() == PID){
                    scanner = device;
                    break;
                }
            }

        }
        return null;
    }  
}
