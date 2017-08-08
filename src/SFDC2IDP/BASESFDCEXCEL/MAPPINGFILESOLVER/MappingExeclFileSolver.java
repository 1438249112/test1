package SFDC2IDP.BASESFDCEXCEL.MAPPINGFILESOLVER;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import SFDC2IDP.BASE.COMMON.CONSTANTS;
import SFDC2IDP.BASE.INTERFACE.IMappingSolver;
public class MappingExeclFileSolver implements IMappingSolver {
	/* (non-Javadoc)
	 * @see SFDC2IDP.BASE.COMMON.IMappingSolver#getSFDCFields()
	 */
	@Override
	public HashMap<String, ArrayList<String>> getSFDCFields() {
		return SFDCFields;
	}
	/* (non-Javadoc)
	 * @see SFDC2IDP.BASE.COMMON.IMappingSolver#setSFDCFields(java.util.HashMap)
	 */
	@Override
	public void setSFDCFields(HashMap<String, ArrayList<String>> sFDCFields) {
		SFDCFields = sFDCFields;
	}
	/* (non-Javadoc)
	 * @see SFDC2IDP.BASE.COMMON.IMappingSolver#getIDPFields()
	 */
	@Override
	public HashMap<String, ArrayList<String>> getIDPFields() {
		return IDPFields;
	}
	/* (non-Javadoc)
	 * @see SFDC2IDP.BASE.COMMON.IMappingSolver#setIDPFields(java.util.HashMap)
	 */
	@Override
	public void setIDPFields(HashMap<String, ArrayList<String>> iDPFields) {
		IDPFields = iDPFields;
	}
	/* (non-Javadoc)
	 * @see SFDC2IDP.BASE.COMMON.IMappingSolver#getIDPFieldTypes()
	 */
	@Override
	public HashMap<String, ArrayList<String>> getIDPFieldTypes() {
		return IDPFieldTypes;
	}
	/* (non-Javadoc)
	 * @see SFDC2IDP.BASE.COMMON.IMappingSolver#setIDPFieldTypes(java.util.HashMap)
	 */
	@Override
	public void setIDPFieldTypes(HashMap<String, ArrayList<String>> iDPFieldTypes) {
		IDPFieldTypes = iDPFieldTypes;
	}
	/* (non-Javadoc)
	 * @see SFDC2IDP.BASE.COMMON.IMappingSolver#setTableNames(java.util.ArrayList)
	 */
	@Override
	public void setTableNames(ArrayList<String> tableNames) {
		this.tableNames = tableNames;
	}
	private ArrayList<String> tableNames = new  ArrayList<String>();
	private HashMap<String,ArrayList<String>> SFDCFields = new  HashMap<String,ArrayList<String>> ();
	private HashMap<String,ArrayList<String>> IDPFields = new  HashMap<String,ArrayList<String>> ();
	private HashMap<String,ArrayList<String>> IDPFieldTypes = new  HashMap<String,ArrayList<String>> ();
	private HashMap<String,ArrayList<String>> sfdcObjectNameS = new  HashMap<String,ArrayList<String>> ();
	private HashMap<String,String> tableName2ObjectName = new  HashMap<String,String> ();
	
	
	public HashMap<String, ArrayList<String>> getSfdcObjectNameS() {
		return sfdcObjectNameS;
	}
	public void setSfdcObjectNameS(
			HashMap<String, ArrayList<String>> sfdcObjectNameS) {
		this.sfdcObjectNameS = sfdcObjectNameS;
	}
	/* (non-Javadoc)
	 * @see SFDC2IDP.BASE.COMMON.IMappingSolver#getTableName2ObjectName()
	 */
	@Override
	public HashMap<String, String> getTableName2ObjectName() {
		return tableName2ObjectName;
	}
	/* (non-Javadoc)
	 * @see SFDC2IDP.BASE.COMMON.IMappingSolver#setTableName2ObjectName(java.util.HashMap)
	 */
	@Override
	public void setTableName2ObjectName(HashMap<String, String> tableName2ObjectName) {
		this.tableName2ObjectName = tableName2ObjectName;
	}
	/* (non-Javadoc)
	 * @see SFDC2IDP.BASE.COMMON.IMappingSolver#getTableNames()
	 */
	@Override
	public ArrayList<String> getTableNames() {
		return tableNames;
	}


	public MappingExeclFileSolver() throws Exception {
		init();
		InputStream is = new FileInputStream(CONSTANTS.SFDC2IDP_Mapping_FilePath);
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
		// 获取每一个工作薄
		for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
			XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
			if (xssfSheet == null) {
				continue;
			}
			if (xssfWorkbook.isSheetHidden(numSheet) == true) {
				continue;
			}
			// 获取当前工作薄的每一行
			String tableName = xssfSheet.getSheetName();
			if(successProxy.contains(tableName.trim().toLowerCase())){
				continue;
			}
			if(!dealTableNames.isEmpty()&&!dealTableNames.contains(tableName.trim().toLowerCase())){
				continue;
			}
			
            tableNames.add(tableName);
            IDPFields.put(tableName, new ArrayList<String>());
            IDPFieldTypes.put(tableName, new ArrayList<String>());
			XSSFRow xssfRow = xssfSheet.getRow(xssfSheet.getFirstRowNum());
			 int sfdcFiledCol = -1;
			 int idpFiledCol = -1;
			 int idpFiledTypeCol = -1;
			 int sfdcObjectNameCol = -1;
//		  System.out.println("Table name : "+tableName);
			for (int col = xssfRow.getFirstCellNum(); col <= xssfRow
					.getLastCellNum(); col++) {
				XSSFCell one = xssfRow.getCell(col);
				if (getValue(one).trim().equalsIgnoreCase("SFDC Object")) {
					for (int rowNum = xssfSheet.getFirstRowNum()+1; rowNum <= xssfSheet
							.getLastRowNum(); rowNum++) {
						XSSFCell cell = xssfSheet.getRow(rowNum)!=null?xssfSheet.getRow(rowNum).getCell(col):null;
					
						String ObjectName = getValue(cell);
						if(ObjectName!=null && !ObjectName.trim().equals("") && !ObjectName.trim().equals("SFDC Object")){
							 tableName2ObjectName.put(tableName, getValue(cell));
							 tableName2ObjectName.put(getValue(cell),tableName);
							 break ;
						}
					}
				}
				if (getValue(one).trim().equalsIgnoreCase("SFDC Field")) {
					sfdcFiledCol = col;
				}
				if (getValue(one).trim().equalsIgnoreCase("SFDC Object")) {
					sfdcObjectNameCol = col;
				}
				
				if (getValue(one).trim().equalsIgnoreCase("IDP Field")) {
					idpFiledCol = col;
				}
				if (idpFiledCol<0 && getValue(one).trim().equalsIgnoreCase("Field")) {
					idpFiledCol = col;
				}
				
				if (getValue(one).trim().equalsIgnoreCase("SQL Data Type")) {
					idpFiledTypeCol = col;
				}
				
			}
//			for (int col = xssfRow.getFirstCellNum(); col <= xssfRow
//					.getLastCellNum(); col++) {
//				XSSFCell one = xssfRow.getCell(col);
//	
//				if (idpFiledTypeCol<0 && getValue(one).trim().equalsIgnoreCase("Data Type")) {
//					idpFiledTypeCol = col;
//				}
//				
//				
//			}
			if(idpFiledTypeCol < 0){
				System.err.println("table "+tableName+" is no data Type filed");
			}
			SFDCFields.put(tableName2ObjectName.get(tableName), new ArrayList<String>());
			sfdcObjectNameS.put(tableName2ObjectName.get(tableName), new ArrayList<String>());
		
			for (int rowNum = xssfSheet.getFirstRowNum()+1; rowNum <= xssfSheet
					.getLastRowNum(); rowNum++) {
				String SFDCField = getValue(xssfSheet.getRow(rowNum)!=null?xssfSheet.getRow(rowNum).getCell(sfdcFiledCol):null);
				String IDPField = getValue(xssfSheet.getRow(rowNum)!=null?xssfSheet.getRow(rowNum).getCell(idpFiledCol):null);
				String IDPFieldType = getValue(xssfSheet.getRow(rowNum)!=null?xssfSheet.getRow(rowNum).getCell(idpFiledTypeCol):null);
				String sfdcObjectNameString = getValue(xssfSheet.getRow(rowNum)!=null?xssfSheet.getRow(rowNum).getCell(sfdcObjectNameCol):null);
				try {
//					String color	= xssfSheet.getRow(rowNum).getCell(sfdcFiledCol).getCellStyle().getFont().getColor()+"";
					boolean isDelete	= xssfSheet.getRow(rowNum).getCell(sfdcFiledCol).getCellStyle().getFont().getStrikeout();
					if(isDelete){
						continue;
					}
//					if(CONSTANTS.colors.containsKey(color)){
//						CONSTANTS.colors.put(color, (Integer.valueOf(CONSTANTS.colors.get(color).split(";")[0])+1)+";"+CONSTANTS.colors.get(color).split(";")[1]+SFDCField+",");
//					}else{
//						CONSTANTS.colors.put(color,"0;start");
//					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			
				if(!SFDCField.equalsIgnoreCase("")&&!IDPField.equals("")){
					if(SFDCField.equalsIgnoreCase("")||IDPField.equals("")||(!SFDCField.trim().equalsIgnoreCase(IDPField.trim()) && !IDPField.trim().equalsIgnoreCase(SFDCField.trim()))){
						System.err.println(tableName2ObjectName.get(tableName)+ " In Mapping Excel  :SFDCField="+SFDCField+"&IDPField="+IDPField);
					}
					 SFDCFields.get(tableName2ObjectName.get(tableName)).add(SFDCField);
					 sfdcObjectNameS.get(tableName2ObjectName.get(tableName)).add(sfdcObjectNameString);
					 
					 IDPFields.get(tableName).add(IDPField);
					 IDPFieldTypes.get(tableName).add(IDPFieldType);
					 
				}
			}
		}
	
	}
	private HashSet<String> successProxy = new HashSet<String> ();
	private HashSet<String> dealTableNames = new HashSet<String> ();
	private void init() {
		try {
			for (String tableName : CONSTANTS.skipDealTableNames.split(",")) {
				successProxy.add(tableName.trim().toLowerCase());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}	
		try {
			for (String tableName : CONSTANTS.dealTableNames.split(",")) {
				if(tableName!=null && !tableName.equalsIgnoreCase("") ){
					dealTableNames.add(tableName.trim().toLowerCase());	
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}	
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