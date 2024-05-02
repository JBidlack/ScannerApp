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

    private JTable jtable = null;
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

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(true);
        setSize(initialW, initialH);
        setLocation(new Point(posH, posW));
        setJMenuBar(menu());

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(panelLayout(), BorderLayout.CENTER);
        // setContentPane(panelLayout());
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
            importFile = openFile();
            if (importFile != null){
                jtable = processFile(importFile);
                // panel.add(processFile(importFile));
                panel.repaint();
            }
        });
        quit.addActionListener(e -> {
            System.exit(0);
        });
        return menu;
    }

    private JPanel panelLayout(){
        panel = new JPanel(new BorderLayout());
        // panel.setLayout(new BorderLayout());
        panel.setPreferredSize(new Dimension(initialW, initialH));
        // panel.setSize(initialW, initialH);
        JScrollPane scroll = new JScrollPane();
        
            scroll.setViewportView(jtable);
        
        panel.add(scroll);

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
            chooser.showOpenDialog(null);
            
            File file = chooser.getSelectedFile();
            if(file != null){
                return file;
            }
            else{
                throw new FileNotFoundException();
            }
        } 
        catch(FileNotFoundException fnf){
            log.error(fnf.getLocalizedMessage());
        }    
        return null;
    }

    private JTable processFile(File excel){
        JTable table = new JTable();
        Workbook book = null;
        Sheet sheet = null;
        try {
            FileInputStream file = new FileInputStream(excel);
            if(file != null){
                book = new XSSFWorkbook(file);
                sheet = book.getSheetAt(0);
                table = createTable(sheet);
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
        return table;
    }

    private JTable createTable(Sheet sheet){
        int counter = 0;
        JTable table = null;
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
            return null;
        }
        int rows = sheet.getPhysicalNumberOfRows()-counter;
        int cols = header.getPhysicalNumberOfCells();

        Object[][] storage = new Object[rows][cols];

        int headerRow = header.getRowNum();

        for (int i = headerRow+1; i < sheet.getLastRowNum()-1; i++){
            Row nextRow = sheet.getRow(i); 
            for (int j = 0; j<cols; j++){
                Cell cell = nextRow.getCell(j);
                if(cell != null){
                    storage[i-3][j] = cell.toString();
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

        table = new JTable(storage, head);






        return table;
    }

}
