package app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.usb.UsbDevice;

import org.apache.logging.log4j.simple.SimpleLogger;
import org.apache.logging.log4j.util.PropertiesUtil;



public class Window extends JFrame{

    private SimpleLogger log = new SimpleLogger("log", null, true, true, true, true, "MM-DD-YYYY", null, PropertiesUtil.getProperties(), null);
    private Dimension screensize    = Toolkit.getDefaultToolkit( ).getScreenSize( );
    private double height = screensize.getHeight();
    private double width = screensize.getWidth();
    private int initialH = (int) height/2;
    private int initialW = (int) width/2;
    private int posH = (int) ((height/2) + (initialH/2))/2;
    private int posW = (int) ((width/2)-(initialW/2))/2;
    private int attempt = 0;
    private JPanel panel = null;
    private JPanel buttPan = null;
    private JPanel inputPan = null;
    private JPanel notice = null;
    public File importFile = null;

    private Scanner device = new Scanner();
    // private UsbDevice scanner =null;
    /**
	 * 
	 */
    // private static ProcessSelection p = new ProcessSelection();
	private static final long serialVersionUID = 1L;

	public Window(){
        
    }
    public void setup(){
        setupMain();
    }

    private void setupMain(){

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(true);
        this.setSize(initialW, initialH);
        this.setLocation(new Point(posH, posW));
        this.setJMenuBar(menu());

        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(panelLayout(), BorderLayout.CENTER);
        
        // setContentPane(panelLayout());
        this.pack();
        this.setVisible(true);
    }

    private JMenuBar menu(){
        
        Font menuFont = new Font("Arial", Font.PLAIN, 14);
        JMenuBar menu = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenuItem open = new JMenuItem("Open File");
        JMenuItem quit = new JMenuItem("Quit");
        
        file.setFont(menuFont);
        open.setFont(menuFont);
        quit.setFont(menuFont);

        file.add(open);
        file.add(quit);
        menu.add(file);
        menu.setFocusTraversalKeysEnabled(true);
        menu.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

        open.addActionListener(e -> {
            ProcessSelection p = new ProcessSelection();
            JLabel label = new JLabel("No Device Connected", JLabel.CENTER);
            importFile = openFile();
            if (importFile != null){
                if(device.scanner == null){
                    while(attempt <= 3 && device.scanner == null){
                        device.retryFind();
                        attempt++;
                    }
                    notice.removeAll();
                    notice.add(label);
                    p.process(importFile, panel);
                }
                if(Scanner.scanner != null){
                    // ProcessSelection p = new ProcessSelection();
                    p.process(importFile, panel);
                }
            }
        });
        quit.addActionListener(e -> {
            System.exit(0);
        });
        return menu;
    }

    private JPanel panelLayout(){
        buttPan = new JPanel();
        inputPan = new JPanel();
        notice = new JPanel();
        device.retryFind();
        
        JLabel scanLabel = new JLabel("Scanned Tag: ");
        JTextField scanInfo = new JTextField(20);
        JButton undo = new JButton("Undo");
        JButton expButton = new JButton("Export");

        buttPan.setLayout(new BoxLayout(buttPan, BoxLayout.Y_AXIS));
        inputPan.setLayout(new BoxLayout(inputPan, BoxLayout.X_AXIS));
        notice.setLayout(new BoxLayout(notice, BoxLayout.X_AXIS));
        // scanInfo.setm
        inputPan.add(scanLabel);
        inputPan.add(scanInfo);
        

        scanInfo.addActionListener(e -> {
            String text = scanInfo.getText();
            ProcessSelection.search(text);

        });
        buttPan.add(Box.createRigidArea(new Dimension(100, 40)));
        buttPan.add(undo);
        buttPan.add(Box.createRigidArea(new Dimension(100, 10)));
        buttPan.add(expButton);
        buttPan.add(Box.createRigidArea(new Dimension(100, 10)));

        undo.setAlignmentX(CENTER_ALIGNMENT);
        expButton.setAlignmentX(CENTER_ALIGNMENT);

        panel = new JPanel(new BorderLayout());
        panel.add(inputPan, BorderLayout.NORTH);
        panel.add(notice, BorderLayout.SOUTH);
        panel.add(buttPan, BorderLayout.EAST);
        panel.setPreferredSize(new Dimension(initialW, initialH));
        noDeviceFound();

        // panel.repaint();

        return panel;
    }


    private File openFile(){
        // String startingPath = System.getProperty("user.home") + "\\Desktop";
        FileSystemView fsv = FileSystemView.getFileSystemView();
        java.io.File dir = fsv.getHomeDirectory();
        FileFilter filter = new FileNameExtensionFilter("Excel Files", "xlsx");
        JFileChooser chooser = new JFileChooser(dir);
        
        try{
            chooser.setFileFilter(filter);
            int f = chooser.showOpenDialog(null);
            // chooser.showOpenDialog(null);
            
            if (f == JFileChooser.APPROVE_OPTION){
                File file = chooser.getSelectedFile();
                if(file != null){
                    return file;
                }
                else{
                    throw new FileNotFoundException();
                }
            }
        } 
        catch(FileNotFoundException fnf){
            log.error(fnf.getLocalizedMessage());
        }    
        return null;
    }

    private void noDeviceFound(){

        if(device.scanner == null){
            JLabel label = new JLabel("No Device Found", JLabel.CENTER);
            notice.add(label);
            panel.revalidate();
            panel.repaint();
        }
    }

    public JPanel getPanel() {
        return panel;
    }
    public void setPanel(JPanel panel) {
        this.panel = panel;
    }
}
