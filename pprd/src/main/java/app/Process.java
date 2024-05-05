// package app;

// import java.awt.BorderLayout;
// import java.awt.Dimension;
// import java.io.File;
// import java.io.FileInputStream;
// import java.io.FileNotFoundException;
// import java.io.IOException;
// import java.util.ArrayList;
// import java.util.HashMap;
// import java.util.Map;

// import javax.swing.JScrollPane;
// import javax.swing.JTable;

// import org.apache.poi.ss.usermodel.Cell;
// import org.apache.poi.ss.usermodel.Row;
// import org.apache.poi.ss.usermodel.Sheet;
// import org.apache.poi.ss.usermodel.Workbook;
// import org.apache.poi.xssf.usermodel.XSSFWorkbook;

// /**
//  * Process
//  */
// public class Process {

//     public Process(){

//     }
//     public void process(File  excel){
//         processFile(excel);
//     }

//     private void processFile(File excel){
        
//         Workbook book = null;
//         Sheet sheet = null;
//         try {
//             FileInputStream file = new FileInputStream(excel);
//             if(file != null){
//                 book = new XSSFWorkbook(file);
//                 sheet = book.getSheetAt(0);
//                 createTable(sheet);
//             }
//         } catch (FileNotFoundException e) {
//             e.printStackTrace();
//         } catch (IOException e) {
//             e.printStackTrace();
//         }
//         finally{
//             if(book != null){
//                 try {
//                     book.close();
//                 } catch (IOException e) {
//                 }
//             }
//         }
//     }

//     private void createTable(Sheet sheet){
//         int counter = 0;
//         Map<Integer, ArrayList<String>> map = new HashMap<>();

//         Row header = null;

//         for (Row row: sheet){
//             if(row.getLastCellNum() > 1){
//                 header = row;
//                 break;
//             }
//             else{
//                 counter++;
//             }
//         }
        
//         if(header == null){
//             System.err.println("No header found");
//             return;
//         }else{
//             int rows = sheet.getPhysicalNumberOfRows()-(counter+1);
//             int cols = header.getPhysicalNumberOfCells();

//             String[][] storage = new String[rows][cols];

//             int headerRow = (header == null) ? 0 : header.getRowNum();

//             for (int i = headerRow+2; i < sheet.getLastRowNum()-1; i++){
//                 Row nextRow = sheet.getRow(i); 
//                 for (int j = 0; j<cols; j++){
//                     Cell cell = nextRow.getCell(j);
//                     if(cell != null){
//                         storage[i-(headerRow+2)][j] = cell.toString();
//                     }
//                     else{
//                         storage[i-(headerRow+2)][j] = "";
//                     }
//                 }
//             }

//             String[] head = new String[cols];
//             for (int a = 0; a < cols; a++){
//                 head[a] = header.getCell(a).toString();
//             }
//             jtable = new JTable(storage, head);
//             jtable.setSize(new Dimension(300, 300));
//             JScrollPane scroll = new JScrollPane();
//             scroll.add(jtable);
//             scroll.setViewportView(jtable);
            
//             scroll.setSize(new Dimension(((initialW)/3)*2, (initialH/3)*2));
            
//             panel.add(scroll, BorderLayout.CENTER);
//             // panel.add(jtable, BorderLayout.CENTER);
//             panel.revalidate();
//             panel.repaint();
//         }
//     }
// }