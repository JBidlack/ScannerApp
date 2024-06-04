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
        makeConnection();
    }

    public static void makeConnection(){
        try {
            localDevice = LocalDevice.getLocalDevice();
            localDevice.setDiscoverable(DiscoveryAgent.GIAC);

            UUID uuid = UUID.randomUUID();//.fromString(SERVER_UUID);
            System.out.println("UUID: " + uuid);
            String url = "btspp://localhost:" + uuid.toString() + ";name=" + SERVER_NAME;

            notifier = (StreamConnectionNotifier) Connector.open(url);
            connection = notifier.acceptAndOpen(); 

            InputStream input = connection.openInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            String number;
            while((number = reader.readLine())!= null){
                System.out.println(number);
            }
            
            input.close();
            reader.close();
            connection.close();
            notifier.close();

            
        } catch (BluetoothStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
}
