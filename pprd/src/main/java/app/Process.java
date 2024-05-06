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
public class Process {

    public Process(){

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
        int counter = 0;
        Map<String, Integer> map = new HashMap<>();

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
                ArrayList<String> list = new ArrayList<>();
                Row nextRow = sheet.getRow(i); 

                for (int j = 0; j<cols; j++){
                    Cell cell = nextRow.getCell(j);

                    if(cell != null){
                        storage[i-(headerRow+2)][j] = cellCheck(cell);
                    }
                    else{
                        storage[i-(headerRow+2)][j] = "";
                    }
                    
                    list.add(storage[i-(headerRow+2)][j]);
                }
                if(list.get(1) != ""){
// Instead of creating a hashmap of index/object, make a hashmap using tag # as the key and index as val?
                    String tag = list.get(1);
                    map.put(tag, i);
                }
                System.out.println(map.size());
            }

            String[] head = new String[cols];
            for (int a = 0; a < cols; a++){
                head[a] = header.getCell(a).toString();
            }
            JTable jtable = new JTable(storage, head);
            jtable.setSize(new Dimension(300, 300));
            
            JScrollPane scroll = new JScrollPane();
            scroll.add(jtable);
            scroll.setViewportView(jtable);
            
            scroll.setSize(new Dimension(((panel.getWidth())/3)*2, (panel.getHeight()/3)*2));
            
            panel.add(scroll, BorderLayout.CENTER);
            // panel.add(jtable, BorderLayout.CENTER);
            panel.revalidate();
            panel.repaint();
        }
    }

    private String cellCheck(Cell cell){
        String check = "";

        if(cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)){
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            Date date = cell.getDateCellValue();
            String newDate = sdf.format(date); //sdf.format(date);
            
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
}