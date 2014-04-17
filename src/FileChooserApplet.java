import java.applet.Applet;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class FileChooserApplet extends Applet{
  
	private static final long serialVersionUID = 2467897672237407225L;
	private static File dataFile;
	
	//Constants
	private static final String DOT = ".";
	private static final String NEW_LINE = "\n";
	private static final String ERR_MSG = "Valid File Types are :";
	private static final String ERR_MSG_TITLE = "Incorrect File Type !";
	
	private static int rowCount=0;
	
	private enum FILE_EXTENSIONS {
		XLS, XLSX;	
	}
	
	public void init(){
		try{
			System.out.println("Applet getting initialized..");
			loadFile();
			readFile();
		}catch(Exception e){
			e.printStackTrace();
		}		
	}
	
	public void loadFile(){
		try{
			while((dataFile=chooseFile())!=null && !validateFile(dataFile)){			
				
		    	StringBuilder errorMessage = new StringBuilder(ERR_MSG);
		    	errorMessage.append(NEW_LINE);
		    	for(FILE_EXTENSIONS extension: FILE_EXTENSIONS.values()){
		    		errorMessage.append(extension.toString().toUpperCase());
		    		errorMessage.append(NEW_LINE);
		    	}
		    	JOptionPane.showMessageDialog(this, errorMessage, ERR_MSG_TITLE, JOptionPane.ERROR_MESSAGE);		    	
		    }
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	
	private File chooseFile() {
		File chosenFile = null;
		try{			
        	JFileChooser chooser = new JFileChooser();
		    chooser.setMultiSelectionEnabled(false);
		    FileNameExtensionFilter filter = new FileNameExtensionFilter(
		            "xls", "xlsx", "XLS", "XLSX");
		    chooser.setFileFilter(filter);
		    int option = chooser.showOpenDialog(chooser);
		    if (option == JFileChooser.APPROVE_OPTION) {
		      chosenFile = chooser.getSelectedFile();
		    }else{
		      chosenFile = null;
		    }
		}catch(Exception e){
			chosenFile = null;
			e.printStackTrace();			
		}			
		return chosenFile;		
	}
	
	private boolean validateFile(File file){
		try{
			String fileName = file.getName();
			int lastIndexOfDot = fileName.lastIndexOf(DOT);
			int fileLength = fileName.length();
			String fileExtension = fileName.substring(lastIndexOfDot + 1,fileLength);		
			for (FILE_EXTENSIONS extension : FILE_EXTENSIONS.values()){
				if(extension.toString().equalsIgnoreCase(fileExtension)){
					return true;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;					
	}
	
	public void readFile(){
		try {
		     
		    FileInputStream file = new FileInputStream(dataFile);
		     
		    //Get the workbook instance for XLS file
		    HSSFWorkbook workbook = new HSSFWorkbook(file);
		 
		    //Get first sheet from the workbook
		    HSSFSheet sheet = workbook.getSheetAt(0);
		    
		    int startRowIndex=1;
		    int endRowIndex=179;
		    int currRowindex= startRowIndex;
		    
		    int startColIndex=0;
		    int endColIndex=18;
		    int currcolIndex=startColIndex;
		    
		    //Iterate through each rows from first sheet
		    Iterator<Row> rowIterator = sheet.iterator();
		    while(rowIterator.hasNext() && currRowindex++ <= endRowIndex) {
		    	//System.out.println("rowCount="+rowCount++);
		        Row row = rowIterator.next();
		         
		        //For each row, iterate through each columns
		        startColIndex=1;
		        Iterator<Cell> cellIterator = row.cellIterator();
		        while(cellIterator.hasNext() && startColIndex++ <= endColIndex) {
		             
		            Cell cell = cellIterator.next();
		            cell.setCellType(Cell.CELL_TYPE_STRING); 
		            if(cell.getStringCellValue()==null ||cell.getStringCellValue().trim().equals("") ){
		            	continue;
		            }
		            
		            System.out.print(cell.getStringCellValue() + "\t\t");
		            /*switch(cell.getCellType()) {
		                case Cell.CELL_TYPE_BOOLEAN:
		                    System.out.print(cell.getBooleanCellValue() + "\t\t");
		                    break;
		                case Cell.CELL_TYPE_NUMERIC:
		                    System.out.print(cell.getNumericCellValue() + "\t\t");
		                    break;
		                case Cell.CELL_TYPE_STRING:
		                    System.out.print(cell.getStringCellValue() + "\t\t");
		                    break;
		                default:    
		                	System.out.print("**EMPTY**\t\t");
		            }*/
		        }
		        System.out.println("");
		    }
		    file.close();	    
		   
		} catch (FileNotFoundException e) {
		    e.printStackTrace();
		} catch (IOException e) {
		    e.printStackTrace();
		}	
	}
}	