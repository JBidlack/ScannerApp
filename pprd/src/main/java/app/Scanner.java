package app;

import java.util.List;

import javax.swing.JLabel;
import javax.usb.UsbDevice;
import javax.usb.UsbDeviceDescriptor;
import javax.usb.UsbException;
import javax.usb.UsbHostManager;
import javax.usb.UsbHub;
import javax.usb.UsbServices;

public class Scanner {
    private static final short VID = 0x18D1;
    private static final short PID = 0x4EE7;
    private static Window window = new Window();
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

    @SuppressWarnings("unchecked")
    private static UsbDevice findDevice(UsbHub root){
        //UsbDevice scanner = null;

        for (UsbDevice device: (List<UsbDevice>) root.getAttachedUsbDevices()){
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
                JLabel label = new JLabel("No Device Found", window.getPanel().getWidth()/2);
                window.getPanel().add(label);
            }

        }
        return scanner;
    }  
}
