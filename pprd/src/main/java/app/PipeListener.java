package app;

import java.nio.charset.StandardCharsets;

import javax.usb.UsbDisconnectedException;
import javax.usb.UsbNotActiveException;
import javax.usb.UsbNotOpenException;
import javax.usb.event.UsbPipeDataEvent;
import javax.usb.event.UsbPipeErrorEvent;
import javax.usb.event.UsbPipeListener;

public class PipeListener implements UsbPipeListener{

    @Override
    public void dataEventOccurred(UsbPipeDataEvent event) {
        byte[] data = event.getData(); // Adjust size as needed

        try {
            String scannedData = new String(data, 0, event.getActualLength(), StandardCharsets.UTF_8).trim();
            System.out.println("Received Data: " + scannedData); // Print received data
        } catch (UsbNotActiveException | UsbNotOpenException | IllegalArgumentException
            | UsbDisconnectedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void errorEventOccurred(UsbPipeErrorEvent event) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'errorEventOccurred'");
    }
    
}
