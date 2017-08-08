package zylon.com.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.UUID;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import edu.emory.mathcs.backport.java.util.Arrays;
import SFDC2IDP.BASE.COMMON.Helper;
public class ExeclFileSolver {
	public static void main(String[] args){
		try {
			ExeclFileSolver e = new ExeclFileSolver();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public ExeclFileSolver() throws Exception {
		InputStream is = new FileInputStream("E:/lenovo-work/work/PARTS SALES API/doc/softwareSource/ECC EDI_20170407.xlsx");
		
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
		// 获取每一个工作薄
		System.out.println("start read excel ... ");
		for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
			XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
			if (xssfSheet == null) {
				continue;
			}
			// 获取当前工作薄的每一行
			String sheetName = xssfSheet.getSheetName();

			XSSFRow xssfRow = xssfSheet.getRow(xssfSheet.getFirstRowNum());
			 int Description = -1;
			 int Business_usage = -1;
			 boolean writeBack = false;
		  System.out.println("sheetName : "+sheetName);
			for (int col = xssfRow.getFirstCellNum(); col <= xssfRow
					.getLastCellNum(); col++) {
				if(col<0){
					writeBack = true;
					continue;
				}
				try {
					System.out.println("col="+col);
					XSSFCell one = xssfRow.getCell(col);
					
					if (getValue(one).trim().equalsIgnoreCase("API Node Name")) {
						Description = col;
					}
					if (getValue(one).trim().equalsIgnoreCase("API Field Name")) {
						Business_usage = col;
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			
				
			}

		if(writeBack){
			 //写到磁盘上
		    try {
		        FileOutputStream fileOutputStream = new FileOutputStream(new File("E:/lenovo-work/work/PARTS SALES API/doc/mappingResult/ECC EDI_"+Helper.getTimeFlag("yyyyMMddhhssmm")+".xlsx"));
		        xssfWorkbook.write(fileOutputStream);
		        fileOutputStream.close();
		    } catch (FileNotFoundException e) {
		        e.printStackTrace();
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		}else{
			for (int rowNum = xssfSheet.getFirstRowNum()+1; rowNum <= xssfSheet
					.getLastRowNum(); rowNum++) {
				String DescriptionStr = getValue(xssfSheet.getRow(rowNum)!=null?xssfSheet.getRow(rowNum).getCell(Description):null);
			
				String Business_usageStr = getValue(xssfSheet.getRow(rowNum)!=null?xssfSheet.getRow(rowNum).getCell(Business_usage):null);
                 if(Helper.isNotNull(DescriptionStr)){
                	 xssfSheet.getRow(rowNum).getCell(Description).setCellValue(getAttrWords(DescriptionStr));
                 }
              if(Helper.isNotNull(Business_usageStr)){
            	  
            	  xssfSheet.getRow(rowNum).getCell(Business_usage).setCellValue(getAttrWords(Business_usageStr));
                 }
			}
		}
		}

	}
	
	private String getAttrWords(String string) {
		
		System.out.println("source="+string);
		String abbrs = "";
		try {
			if(Helper.isNotNull(string)){
				string = specialHandle(string);
				
				if(string.length()>1){
					String words [] = string.split("\\W+");
					System.out.println("words="+Arrays.toString(words));
					for (String word : words) {
						if(Helper.isNotNull(word)){
							word = word.trim();
							  HashSet<String>	abbrWords= abbreviations.getAbbrWords(word);
							   for (String abbrWord : abbrWords) {
								   abbrs+=abbrWord+"/";
							       }
							   if(abbrs.endsWith("/")){
								   abbrs = abbrs.substring(0, abbrs.length()-1);
							   }
							            abbrs+="_";
									}
						}
			 
				}else{
					String words [] = string.split("\\s+");
					for (int i = 0; i < words.length; i++) {
						abbrs += words[i]+"_";
					}
					
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		 if(abbrs.endsWith("_")){
			   abbrs = abbrs.substring(0, abbrs.length()-1);
		   }
		System.out.println(abbrs);
		
		String reversalStrings = "_in_,_of_";
		for (String keyString : reversalStrings.split(",")) {
			if(abbrs.contains(keyString)){
				String words[] = abbrs.split(keyString);
				abbrs = words[1]+"_"+words[0];
			}
		}
		
		return " "+abbrs.toLowerCase();
	}
	private String specialHandle(String string) {
		if(string.equalsIgnoreCase("qualifier")){
			return "";
		}
		string = string.trim().toLowerCase();
		string = string.replaceAll("purchas\\w+\\s+order", "po")
				.replaceAll("postal\\w+\\s+code", "zip")
				.replaceAll("contact\\w+\\s+person", "Contacter")
				.replaceAll("\\(.*\\)", "");
		 
		return string.trim();
	}
	// 转换数据格式
	private String getValue(XSSFCell xssfRow) {
		String result = "";
		try {
			if (xssfRow.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
				result = String.valueOf(xssfRow.getBooleanCellValue());
			} else if (xssfRow.getCellType() == Cell.CELL_TYPE_NUMERIC) {
				result =  String.valueOf(xssfRow.getNumericCellValue());
			} else {
				result =  String.valueOf(xssfRow.getStringCellValue());
			}
		} catch (Exception e) {

		}
		return result.trim();

	}
}