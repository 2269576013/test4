package utility;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import au.com.bytecode.opencsv.CSVReader;

public class ExcelData {
		
	public static XSSFSheet sheet;
	public static Map<String,String> testInputData = getAllMapTestData();
	/**
	 * 根据sheetname定位到sheet，后面定位单元格cell需要用sheet
	 * @param filepath
	 * @param sheetname
	 */
	public static void setPath(String filepath,String sheetname) {
		FileInputStream fs ;
		try {
		fs = new FileInputStream(filepath);
		XSSFWorkbook workbook = new XSSFWorkbook(fs);
		sheet = workbook.getSheet(sheetname);
		}catch (IOException e ){
			Log.error("Package:Utility||Class ExcelData||Method setPath"+ e.getMessage()); 
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据行数和列数取单元格的值
	 * @param row
	 * @param col
	 * @return
	 */
	public static String getCellData(int row, int col){
		XSSFCell cell = sheet.getRow(row).getCell(col);
		String cellvalue = null;
		switch(cell.getCellType()){
		case XSSFCell.CELL_TYPE_NUMERIC:
			cellvalue = cell.getRawValue();
			break;
		case XSSFCell.CELL_TYPE_STRING:
			cellvalue = cell.getStringCellValue();
			break;
		case XSSFCell.CELL_TYPE_BLANK:
			cellvalue = "";
			break;
		default:
			Log.warn("Excel data type does not exist.Cell type is " + cell.getCellType());
		}
		
		return cellvalue;
				
	}
	
	/**
	 * 根据key值所在的行数，getCellData()取测试数据的时候要用行数
	 * @param key
	 * @param column
	 * @return
	 */
	public static int getRowContains(String key, int column){
	//一般column是常数0，第0列放key值，第1列放测试数据。column从0开始数
		int i;
		int rowcount = sheet.getLastRowNum();
		//rownumber从0开始数
		for(i=1;i<rowcount;i++){
			if(ExcelData.getCellData(i, column).equalsIgnoreCase(key)){
				break;
			}
		}
		if(i>=rowcount){
			Log.error("[getRowContains]:Can not find" + key);
		}
		return i;
		
	}
	
	
	
	
	/**
	 * 如果不用上面3个方法，还可以把excel表里的数据存成 hash map,把key和value 存成键值对的形式
	 * @return
	 */
	public static Map<String,String> getAllMapTestData(){
		Map<String,String> mapData = new HashMap<String,String>();
		setPath(Constants.PATH+Constants.FILENAME, Constants.SHEETNAME);
		int rowNumber = sheet.getPhysicalNumberOfRows();
		XSSFCell cell1=null;
		XSSFCell cell2=null;
		for(int i=1;i<rowNumber;i++){
			cell1=sheet.getRow(i).getCell(Constants.KEYCOLUMN);
			if(cell1.getCellType()!=XSSFCell.CELL_TYPE_BLANK){
				String keyValue = cell1.getStringCellValue().trim().toLowerCase();
				String value;
				cell2 = sheet.getRow(i).getCell(Constants.COLUMN);
				switch(cell2.getCellType()){
				case XSSFCell.CELL_TYPE_NUMERIC:			
					value = cell2.getRawValue().toString().trim().toLowerCase();
					mapData.put(keyValue, value);
					break;
				case XSSFCell.CELL_TYPE_STRING:
					value = cell2.getStringCellValue().trim().toLowerCase();
					mapData.put(keyValue, value);
					break;
				case XSSFCell.CELL_TYPE_BLANK:
					value = "";
					mapData.put(keyValue, value);
					break;
				default:
					Log.warn("Excel data type does not exist. Cell type is: " +cell2.getCellType());
				}
				
				
			}
		}
		return mapData;
		
	}
	
	
	/**
	 * 从map中根据key去除value值
	 * @param skey
	 * @return
	 */
	public static String getMapData (String skey){
	//	Map<String,String> data = getAllMapTestData();
		return testInputData.get(skey.toLowerCase());
		
	}  
	
	
	
	/**
	 * 用csvReader读取csv文件保存的测试数据
	 * @return
	 */
	public static List<String[]> getTestDataFromObjectsFile(){
		CSVReader csvReader;
		List<String[]> lists =new ArrayList<>();
				
		try {
				csvReader = new CSVReader(new FileReader(Constants.PATH + Constants.CSVFILENAME));
				lists = csvReader.readAll();
				//read文件一定要记得关闭，否则会内存泄露
				csvReader.close();
			} catch (IOException e) {
				Log.error("ExcelData||getTestDataFromObjectsFile: cannot get data from csv file");
				e.printStackTrace();
			} 
				
		return lists;
	}
	
	
	/**
	 * 读取测试文件中的locator
	 * @return
	 */
	public static List<String[]> getLocatorsFromObjectsFile(){
		CSVReader csvReader;
		List<String[]> lists =new ArrayList<>();
				
		try {
				csvReader = new CSVReader(new FileReader(Constants.PATH + Constants.OBJECTFILE));
				lists = csvReader.readAll();
				//read文件一定要记得关闭，否则会内存泄露
				csvReader.close();
			} catch (IOException e) {
				Log.error("Fail to get the web locators from ObjectRepository file. ");
				e.printStackTrace();
			} 
				
		return lists;
	}

}
