package app;

import java.util.List;

import javax.usb.UsbConfiguration;
import javax.usb.UsbDevice;
import javax.usb.UsbDeviceDescriptor;
import javax.usb.UsbEndpoint;
import javax.usb.UsbException;
import javax.usb.UsbHostManager;
import javax.usb.UsbHub;
import javax.usb.UsbInterface;
import javax.usb.UsbInterfacePolicy;
import javax.usb.UsbPipe;
import javax.usb.UsbServices;

public class Scanner {
    private static final short VID = 0x18D1;
    private static final short PID = 0x4EE7;
    protected static UsbDevice scanner = null;
    private static UsbHub root = null;
    private static UsbConfiguration configuration = null;
    private static UsbEndpoint endpoint = null;
    private static UsbInterface usbInterface = null;
    private static UsbPipe pipe = null;

    public Scanner(){
        try {
            UsbServices service = UsbHostManager.getUsbServices();

            root = service.getRootUsbHub();

            scanner = findDevice(root);

            configuration = scanner.getActiveUsbConfiguration();
            usbInterface = configuration.getUsbInterface((byte) 0);


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
