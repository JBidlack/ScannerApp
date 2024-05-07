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
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import org.apache.logging.log4j.core.config.builder.api.Component;
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
    private JPanel panel = null;
    private JPanel buttPan = null;
    private JPanel inputPan = null;
    public File importFile = null;
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Window(){
        setup();
    }

    private void setup(){

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(true);
        this.setSize(initialW, initialH);
        this.setLocation(new Point(posH, posW));
        this.setJMenuBar(menu());

        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(panelLayout(), BorderLayout.CENTER);
        // setContentPane(panelLayout());
        // this.pack();
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
            importFile = openFile();
            if (importFile != null){
                Process p = new Process();
                p.process(importFile, panel);
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
        JLabel scanLabel = new JLabel("Scanned Tag: ");
        JTextField scanInfo = new JTextField();

        JButton undo = new JButton("Undo");
        JButton expButton = new JButton("Export");
        buttPan.setLayout(new BoxLayout(buttPan, BoxLayout.Y_AXIS));
        inputPan.setLayout(new BoxLayout(inputPan, BoxLayout.X_AXIS));
        inputPan.add(scanLabel);
        inputPan.add(scanInfo);
        buttPan.add(Box.createRigidArea(new Dimension(100, 40)));
        buttPan.add(undo);
        buttPan.add(Box.createRigidArea(new Dimension(100, 10)));
        buttPan.add(expButton);
        buttPan.add(Box.createRigidArea(new Dimension(100, 10)));

        undo.setAlignmentX(CENTER_ALIGNMENT);
        expButton.setAlignmentX(CENTER_ALIGNMENT);

        panel = new JPanel(new BorderLayout());
        panel.add(inputPan, BorderLayout.NORTH);
        panel.add(buttPan, BorderLayout.EAST);
        panel.setPreferredSize(new Dimension(initialW, initialH));
        panel.repaint();

        return panel;
    }
    // private JPanel buttonPanel(){
    //     buttPan = new 
    // }

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
}
