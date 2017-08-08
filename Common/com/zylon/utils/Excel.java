package com.zylon.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Excel {
	private 	HashMap<String,HashMap<String,ArrayList<Cell>>> sheets = new 	HashMap<String,HashMap<String,ArrayList<Cell>>> ();
	private 	String path  = "";
    public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public ArrayList<Cell> getColsByFirstColName(String sheetName ,String colName){
		for (Entry<String, HashMap<String, ArrayList<Cell>>> entry : sheets.entrySet()) {
            if(!sheetName.trim().equalsIgnoreCase(entry.getKey().trim())){
            	continue;
            }
			for (Entry<String, ArrayList<Cell>> cells : entry.getValue().entrySet()) {
				for (Cell cell : cells.getValue()) {
					if(this.getValue(cell).trim().equalsIgnoreCase(colName.trim())){
						return cells.getValue();
					}
//					System.out.println(this.getValue(cell));
				}
			}
		}
		return null;
		}
	public ArrayList<Cell> getColsByBlurFirstColName(String sheetName ,String colName){
		for (Entry<String, HashMap<String, ArrayList<Cell>>> entry : sheets.entrySet()) {
            if(!sheetName.trim().equalsIgnoreCase(entry.getKey().trim())){
            	continue;
            }
			for (Entry<String, ArrayList<Cell>> cells : entry.getValue().entrySet()) {
				for (Cell cell : cells.getValue()) {
					if(this.getValue(cell).trim().contains(colName.trim())){
						return cells.getValue();
					}
//					System.out.println(this.getValue(cell));
				}
			}
		}
		return null;
		}
	public ArrayList<Cell> getColsByFirstColName(String colName){
		for (Entry<String, HashMap<String, ArrayList<Cell>>> entry : sheets.entrySet()) {
			for (Entry<String, ArrayList<Cell>> cells : entry.getValue().entrySet()) {
				for (Cell cell : cells.getValue()) {
					if(this.getValue(cell).trim().equalsIgnoreCase(colName.trim())){
						return cells.getValue();
					}
//					System.out.println(this.getValue(cell));
				}
			}
		}
		return null;
		}
		
	public void write(){
  	  try {
		        FileOutputStream fileOutputStream = new FileOutputStream(new File(path));
		        xssfWorkbook.write(fileOutputStream);
		        fileOutputStream.close();
		    } catch (FileNotFoundException e) {
		        e.printStackTrace();
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
  }
	public void write(String path){
  	  try {
		        FileOutputStream fileOutputStream = new FileOutputStream(new File(path));
		        xssfWorkbook.write(fileOutputStream);
		        fileOutputStream.close();
		    } catch (FileNotFoundException e) {
		        e.printStackTrace();
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
  }
	public static void main(String[] args) {
		Excel excel	= new  Excel("E:/lenovo-work/work/SFDC2UDP/resources/UDP table .xlsx");
	     ArrayList<Cell>	cells  = excel.getColsByFirstColName("鐢熶骇鐜-闈瀌elete琛�","sfdc_ms_la_account_idl");
	     for (Cell cell : cells) {
				System.out.println(excel.getValue(cell));
			}
	}
	
	
	public Excel(String path) {
		this.path = path;
		this.read(path);
	}


	public HashMap<String, HashMap<String, ArrayList<Cell>>> getSheets() {
		return sheets;
	}


	public void setSheets(HashMap<String, HashMap<String, ArrayList<Cell>>> sheets) {
		this.sheets = sheets;
	}

	private 	 XSSFWorkbook xssfWorkbook = null;
	public XSSFWorkbook getXssfWorkbook() {
		return xssfWorkbook;
	}


	public void setXssfWorkbook(XSSFWorkbook xssfWorkbook) {
		this.xssfWorkbook = xssfWorkbook;
	}

	private  void read(String path) {
		 HashMap<String,HashMap<String,ArrayList<Cell>>> excels = new   HashMap<String,HashMap<String,ArrayList<Cell>>>();
		InputStream is;
		try {
			is = new FileInputStream(path);
		
			xssfWorkbook = new XSSFWorkbook(is);
			
		// 锟斤拷取每一锟斤拷锟斤拷锟斤拷锟斤拷
		for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
			XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
			if (xssfSheet == null) {
				continue;
			}
			if (xssfWorkbook.isSheetHidden(numSheet) == true) {
				continue;
			}
			// 锟斤拷取锟斤拷前锟斤拷锟斤拷锟斤拷锟斤拷每一锟斤拷
			String tableName = xssfSheet.getSheetName();
			HashMap<String,ArrayList<Cell>> sheets = new HashMap<String,ArrayList<Cell>>();
			excels.put(tableName,sheets);
			Iterator<Row> rowIter = xssfSheet.iterator();
			while(rowIter.hasNext()){
				Row	row = rowIter.next();
				Iterator<Cell> cellIter = row.cellIterator();
			while(cellIter.hasNext()){
			
				Cell cell = cellIter.next();
				if(sheets.get(cell.getColumnIndex()+"")==null){
					sheets.put(cell.getColumnIndex()+"", new ArrayList<Cell>());
				}
				sheets.get(cell.getColumnIndex()+"").add(cell);
			}
			}
		}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		this.setSheets(excels);
	
	}

	public  String getValue(Cell cell) {
		String result = "";
		try {
			if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
				result = String.valueOf(cell.getBooleanCellValue());
			} else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
				result =  String.valueOf(cell.getNumericCellValue());
			} else {
				result =  String.valueOf(cell.getStringCellValue());
			}
		} catch (Exception e) {

		}
		return result.trim();

	}
	public ArrayList<Cell> getColsByBlurFirstColName(String colName){
		for (Entry<String, HashMap<String, ArrayList<Cell>>> entry : sheets.entrySet()) {
			for (Entry<String, ArrayList<Cell>> cells : entry.getValue().entrySet()) {
				for (Cell cell : cells.getValue()) {
					if(this.getValue(cell).toLowerCase().contains(colName.toLowerCase())){
						return cells.getValue();
					}
//					System.out.println(this.getValue(cell));
				}
			}
		}
		return null;
		}
}