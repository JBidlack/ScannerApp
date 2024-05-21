package app;

import java.awt.Panel;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
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

            if( scanner == null){
                noDeviceFound();
            }

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

    private void noDeviceFound(){
        
        SwingUtilities.invokeLater(() -> {
            if (window != null && window.getPanel() != null) {
            JLabel label = new JLabel("No Device Found", JLabel.CENTER);
            JPanel panel = window.getPanel();
            panel.add(label);
            panel.revalidate();
            panel.repaint();
        } else {
            System.err.println("Window or panel is null" + window + " PPPPPPPPPPPPPPP " + window.getPanel());
        }
    });
    }
}
