package app;

import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class Window extends JFrame{
    public File excel;
    private Dimension screensize    = Toolkit.getDefaultToolkit( ).getScreenSize( );
    private double height = screensize.getHeight();
    private double width = screensize.getWidth();
    private int initialH = (int) height/2;
    private int initialW = (int) height/2;
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Window(){
        setup();
    }

    private void setup(){

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(true);
        setSize(initialW, initialH);
        setJMenuBar(menu());
        setContentPane(panelLayout());
        
        setVisible(true);
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
            excel = openFile();
            processFile(excel);
        });
        quit.addActionListener(e -> {
            System.exit(0);
        });
        return menu;
    }

    private JPanel panelLayout(){
        
        JPanel panel = new JPanel();
        panel.setMinimumSize(getSize());
        panel.setSize(initialW, initialH);
        return panel;
    }

    private File openFile(){
        String startingPath = System.getProperty("user.home") + "\\Desktop";
        FileFilter filter = new FileNameExtensionFilter("Excel Files", "xlsx");
        JFileChooser chooser = new JFileChooser();
        
        chooser.setFileFilter(filter);
        chooser.showOpenDialog(null);
        File file = chooser.getSelectedFile();
       
        return file;
    }

    private JTable processFile(File excel){
        JTable table = new JTable();
        try {
            FileInputStream file = new FileInputStream(excel);
            if(file != null){
                Workbook book = new XSSFWorkbook(file);
                Sheet sheet = book.getSheetAt(0);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return table;
    }

}
