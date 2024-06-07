package app;

import javax.bluetooth.*;
import javax.microedition.io.*;
import java.io.*;
import java.util.UUID;

public class Bluetooth {
    private static final String SERVER_UUID = "1101";
    private static final String SERVER_NAME = "BTServer";
    private static LocalDevice localDevice;
    private static StreamConnectionNotifier notifier;
    private static StreamConnection connection;
    private static InputStream input = null;
    private static BufferedReader reader = null;
    private BluetoothConnectionListener listener;
    
    public Bluetooth(BluetoothConnectionListener listener){
        this.listener = listener;
    }

    public static void start(){
        makeConnection();
    }

    private static void makeConnection(){
        try {
            localDevice = LocalDevice.getLocalDevice();
            localDevice.setDiscoverable(DiscoveryAgent.GIAC);

            UUID uuid = UUID.randomUUID();//.fromString(SERVER_UUID);
            System.out.println("UUID: " + uuid);
            String url = "btspp://b4:29:3d:5e:3b:88:1;authenticate=false;encrypt=false;master=false";

            notifier = (StreamConnectionNotifier) Connector.open(url);
            new Thread(() -> {
                try{
                    connection = notifier.acceptAndOpen(); 

                    input = connection.openInputStream();
                    reader = new BufferedReader(new InputStreamReader(input));
                    String number;
                    while((number = reader.readLine())!= null){
                        System.out.println(number);
                    }
                } catch(IOException ioe){
                    ioe.printStackTrace();
                }
            }).start();
   
        } catch (BluetoothStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }

    public void stop(){
        try {
            if(input != null){
                input.close();
            }
            if(reader != null){
                reader.close();
            }
            if(connection != null){
                connection.close();
            }
            if(notifier != null){
                notifier.close();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public interface BluetoothConnectionListener {
        void onDataReceoved(String data);
        void onError(String errorMsg);
    
        
    }
}
