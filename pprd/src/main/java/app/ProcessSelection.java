package app;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Process
 */
public class ProcessSelection {
    private static Map<String, Integer> map = null;
    private static JTable jtable = null;

    public ProcessSelection(){

    }
    public void process(File  excel, JPanel panel){
        processFile(excel, panel);
    }

    private void processFile(File excel, JPanel panel){
        
        Workbook book = null;
        Sheet sheet = null;
        try {
            FileInputStream file = new FileInputStream(excel);
            if(file != null){
                book = new XSSFWorkbook(file);
                sheet = book.getSheetAt(0);
                createTable(sheet, panel);
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

    private void createTable(Sheet sheet, JPanel panel){
        int headerIndex = findHeader(sheet);
        map = new HashMap<>();
        Row header = null;

        if(headerIndex == -1){
            System.err.println("No header found");
            return;
        }
        
        header = sheet.getRow(headerIndex);
        int rows = sheet.getPhysicalNumberOfRows()-(headerIndex+1);
        int cols = header.getPhysicalNumberOfCells();

        String[][] storage = new String[rows][cols];

        int headerRow = (header == null) ? 0 : header.getRowNum();

        for (int i = headerRow+1; i < sheet.getLastRowNum(); i++){
            Row nextRow = sheet.getRow(i); 

            for (int j = 0; j<cols; j++){
                Cell cell = nextRow.getCell(j);
                storage[i-(headerRow+1)][j] = cell != null ? cellCheck(cell) : "";
            }
        }

        String[] head = new String[cols];

        for (int a = 0; a < cols; a++){
            head[a] = header.getCell(a).toString();
        }

        DefaultTableModel model = new DefaultTableModel(storage, head){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };

        jtable = new JTable(model); 

        createMap();
        
        JScrollPane scroll = new JScrollPane(jtable);
        
        scroll.setSize(new Dimension(((panel.getWidth())/3)*2, (panel.getHeight()/3)*2));
        
        panel.add(scroll, BorderLayout.CENTER);
        panel.revalidate();
        panel.repaint();
    }

    private int findHeader(Sheet sheet){
        for (Row row: sheet){
            if(row.getLastCellNum() > 1){
                return row.getRowNum();
            }
        }
        return -1;
    }

    private void createMap(){
        map.clear();
        for (int r = 0; r< jtable.getRowCount(); r++){
            String value = String.valueOf(jtable.getValueAt(r, 1));
            if(jtable.getValueAt(r, 1) != ""){
                map.put(value, r);
            }
        }
    }

    private String cellCheck(Cell cell){
        String check = "";
        // Process process = Runtime.getRuntime();

        if(cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)){
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            Date date = cell.getDateCellValue();
            String newDate = sdf.format(date); 
            
            check = newDate;
        }
        else if(cell.getCellType() == CellType.NUMERIC){
            int value = (int) cell.getNumericCellValue();
            check = String.valueOf(value);
        }
        else{
            check = cell.toString();
        }
        return check;
    }

    public static void search(String item){
        itemSearch(item);
    }

    private static void itemSearch(String item){
        int rowNum = 0;
        
        if(map != null){
            if(map.containsKey(item)){
                rowNum = map.get(item);
                Object items = jtable.getValueAt(rowNum, jtable.getColumnCount()-1);
                int value = Integer.valueOf(String.valueOf(items));
                jtable.setValueAt(value-1, rowNum, jtable.getColumnCount()-1);;
                System.out.println(items + " " +map.get(item) + " " + jtable.getValueAt(rowNum, 2));
            }
        }
    }

    // private static void printItem(){
    //     if (Scanner.scanner != null){
    //         byte[] data = new byte[64];
    //         int received;
    //         try {
    //             received = Scanner.pipe.syncSubmit(data);
                
    //             if (received > 0) {
    //                 String scannedData = new String(data, 0, received);
    //                 System.out.println(scannedData);
    //             }
    //         } catch (UsbNotActiveException | UsbNotOpenException | IllegalArgumentException | UsbDisconnectedException
    //         | UsbException e) {
    //             // TODO Auto-generated catch block
    //             e.printStackTrace();
    //         }
    //     }
    // }
}