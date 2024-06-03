package app;

import javax.bluetooth.*;
// import javax.bluetooth.DiscoveryAgent;
// import javax.bluetooth.LocalDevice;
import javax.microedition.io.*;
import java.io.*;
// import javax.microedition.io.StreamConnectionNotifier;
import java.util.UUID;

public class Bluetooth {
    private static final String SERVER_UUID = "1101";
    private static final String SERVER_NAME = "BTServer";
    private static LocalDevice localDevice;
    private static StreamConnectionNotifier notifier;
    private static StreamConnection connection;
    
    public Bluetooth(){
    }

    public static void makeConnection(){
        try {
            localDevice = LocalDevice.getLocalDevice();
            localDevice.setDiscoverable(DiscoveryAgent.GIAC);

            UUID uuid = UUID.fromString(SERVER_UUID);
            String url = "btspp://localhost:" + uuid.toString() + ";name=" + SERVER_NAME;

            notifier = (StreamConnectionNotifier) Connector.open(url);
            connection = notifier.acceptAndOpen(); 

        } catch (BluetoothStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
}
