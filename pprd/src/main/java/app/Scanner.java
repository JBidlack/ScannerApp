package app;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.util.List;

import javax.usb.UsbConfiguration;
import javax.usb.UsbConst;
import javax.usb.UsbDevice;
import javax.usb.UsbDeviceDescriptor;
import javax.usb.UsbDisconnectedException;
import javax.usb.UsbEndpoint;
import javax.usb.UsbException;
import javax.usb.UsbHostManager;
import javax.usb.UsbHub;
import javax.usb.UsbInterface;
import javax.usb.UsbInterfacePolicy;
import javax.usb.UsbNotActiveException;
import javax.usb.UsbNotOpenException;
import javax.usb.UsbPipe;
import javax.usb.UsbServices;
import javax.usb.event.UsbPipeDataEvent;
import javax.usb.event.UsbPipeErrorEvent;
import javax.usb.event.UsbPipeListener;

public class Scanner {
    private static final short VID = 0x18D1;
    private static final short PID = 0x4EE7;
    protected static UsbDevice scanner = null;
    private static UsbHub root = null;
    private static UsbConfiguration configuration = null;
    private static UsbEndpoint endpoint = null;
    private static UsbInterface usbInterface = null;
    private static UsbPipe pipe = null;
    private final int port = 00000;

    public Scanner(){
        BufferedReader br = null;
        try (ServerSocket serverSocket = new ServerSocket(port)){
            UsbServices service = UsbHostManager.getUsbServices();

            root = service.getRootUsbHub();

            scanner = findDevice(root);

            // printDeviceInfo(scanner);

            if(scanner != null){
                configuration = scanner.getActiveUsbConfiguration();
                usbInterface = configuration.getUsbInterface((byte) 0);

                usbInterface.claim(usbInterface1 -> true);

                endpoint = getEndpoint(usbInterface);

                System.out.println(endpoint);
                if(endpoint != null){
                    pipe = endpoint.getUsbPipe();
                    pipe.open(); 
                    br = new BufferedReader(new InputStreamReader(null));

                    
                    // Start a thread to continuously read data
// Thread readThread = new Thread(() -> {
//     try {
//         while (true) {
//             byte[] data = new byte[64]; // Adjust size as needed
//             int received = pipe.syncSubmit(data);
//             if (received > 0) {
//                 String scannedData = new String(data, 0, received, "UTF-8").trim();
//                 System.out.println("Received Data: " + scannedData); // Print received data
               
//             }
//         }
//     } catch (UsbException | UnsupportedEncodingException e) {
//         e.printStackTrace();
//     }
// });

                    // readThread.start();

                    // Runtime.getRuntime().addShutdownHook(new Thread(() ->{
                    //     try{
                    //         pipe.close();
                    //         usbInterface.release();
                    //     }catch (UsbException e){
                    //         e.printStackTrace();
                    //     }
                    // }));
                }
            }
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UsbException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public static UsbDevice retryFind(){
        findDevice(root);
        return null;
    }

    public static void closePipe(){
        try {
            if(pipe != null && pipe.isOpen()){
                pipe.close();
            }
        } catch (UsbNotActiveException | UsbNotOpenException | UsbDisconnectedException | UsbException e) {
            e.printStackTrace();
        }
    } 

    private void translate(String b){
        // Translate the scanned data here
        System.out.println("Scanned Data: " + b);
        
    }

    @SuppressWarnings("unchecked")
    private static UsbEndpoint getEndpoint(UsbInterface usb){
        for (UsbEndpoint endpoint: (List<UsbEndpoint>) usb.getUsbEndpoints()){
            byte endpointAddress = endpoint.getUsbEndpointDescriptor().bEndpointAddress();
            if((endpointAddress & 0x80) != 0){
                return endpoint;
            }
        }
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
                    return scanner;
                }
            }

        }
        return null;
    }  

    // private static UsbEndpoint getEndpoint(UsbInterface usbInterface, byte type, byte direction) {
    //     for (UsbEndpoint endpoint : (List<UsbEndpoint>) usbInterface.getUsbEndpoints()) {
    //         if (endpoint.getType() == type && endpoint.getDirection() == direction) {
    //             return endpoint;
    //         }
    //     }
    //     return null;
    // }
}
