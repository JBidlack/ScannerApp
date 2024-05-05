package app;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import org.apache.logging.log4j.simple.SimpleLogger;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class Window extends JFrame{

    private SimpleLogger log = new SimpleLogger("log", null, true, true, true, true, "MM-DD-YYYY", null, PropertiesUtil.getProperties(), null);
    private Dimension screensize    = Toolkit.getDefaultToolkit( ).getScreenSize( );
    private double height = screensize.getHeight();
    private double width = screensize.getWidth();
    private int initialH = (int) height/2;
    private int initialW = (int) width/2;
    private int posH = (int) ((height/2) + (initialH/2))/2;
    private int posW = (int) ((width/2)-(initialW/2))/2;

    private JTable jtable;
    private JPanel panel = null;
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
                processFile(importFile);
            }
        });
        quit.addActionListener(e -> {
            System.exit(0);
        });
        return menu;
    }

    private JPanel panelLayout(){
        panel = new JPanel(new BorderLayout());

        panel.setPreferredSize(new Dimension(initialW, initialH));
        
        panel.repaint();

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

    private void processFile(File excel){
        
        Workbook book = null;
        Sheet sheet = null;
        try {
            FileInputStream file = new FileInputStream(excel);
            if(file != null){
                book = new XSSFWorkbook(file);
                
                sheet = book.getSheetAt(0);
                createTable(sheet);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally{
            if(book != null){
                try {
                    book.close();
                } catch (IOException e) {
                }
            }
        }
    }

    private void createTable(Sheet sheet){
        int counter = 0;
        Map<Integer, ArrayList<String>> map = new HashMap<>();

        Row header = null;

        for (Row row: sheet){
            
            if(row.getLastCellNum() > 1){
                header = row;
                break;
            }
            else{
                counter++;
            }
        }
        
        if(header == null){
            System.err.println("No header found");
            return;
        }else{
            int rows = sheet.getPhysicalNumberOfRows()-(counter+1);
            int cols = header.getPhysicalNumberOfCells();

            String[][] storage = new String[rows][cols];

            int headerRow = (header == null) ? 0 : header.getRowNum();

            for (int i = headerRow+2; i < sheet.getLastRowNum()-1; i++){
                Row nextRow = sheet.getRow(i); 
                for (int j = 0; j<cols; j++){
                    Cell cell = nextRow.getCell(j);
                    if(cell != null){
                        storage[i-(headerRow+2)][j] = cell.toString();
                    }
                    else{
                        storage[i-3][j] = "";
                    }
                }
            }

            String[] head = new String[cols];
            for (int a = 0; a < cols; a++){
                head[a] = header.getCell(a).toString();
            }
            jtable = new JTable(storage, head);
            jtable.setSize(new Dimension(300, 300));
            JScrollPane scroll = new JScrollPane();
            scroll.add(jtable);
            scroll.setViewportView(jtable);
            
            scroll.setSize(new Dimension(((initialW)/3)*2, (initialH/3)*2));
            
            panel.add(scroll, BorderLayout.CENTER);
            // panel.add(jtable, BorderLayout.CENTER);
            panel.revalidate();
            panel.repaint();
        }
    }
}
