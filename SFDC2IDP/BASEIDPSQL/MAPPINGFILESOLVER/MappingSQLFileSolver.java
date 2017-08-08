package SFDC2IDP.BASEIDPSQL.MAPPINGFILESOLVER;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import edu.emory.mathcs.backport.java.util.Arrays;
import SFDC2IDP.BASE.COMMON.CONSTANTS;
import SFDC2IDP.BASE.COMMON.Helper;
import SFDC2IDP.BASE.INTERFACE.IMappingSolver;
public class MappingSQLFileSolver implements IMappingSolver {
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
	private HashMap<String,String> tableName2ObjectName = new  HashMap<String,String> ();
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

	public MappingSQLFileSolver() throws Exception {
        File createSqlsFile = new File(CONSTANTS.IDP_CreateTable_FilePath);
	      String createSqls = Helper.getFileContent(createSqlsFile);
	      String[] arrayCreateSqls = createSqls.split("CREATE TABLE");
//			System.out.println(createSqls);
			for (String creatSqlString : arrayCreateSqls) {
				if(creatSqlString.length()>10){
					try {
						parseSQL(creatSqlString);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			
			
		
	
	}
	private void parseSQL(String creatSqlString) {
		creatSqlString = creatSqlString.substring(0,
				creatSqlString.lastIndexOf("NULL")).trim();
		// 获取表名
		String tableName = creatSqlString.split("\\s")[0].trim();
		String suffix = "dbo.";
		if(tableName.startsWith(suffix)){
			tableName = tableName.substring(suffix.length()).replace("_Temp", "");
		}
		System.out.println("table name = " + tableName);
		 tableNames.add(tableName);
       
        
		// 获取字段
	
		String fieldPart = creatSqlString.substring(
				creatSqlString.indexOf("(") + 1, creatSqlString.length())
				.trim();

		String fields[] = fieldPart.split("NULL\\,");
//		System.out.println("fields count = " + fields.length);
//		System.out.println("fields = " + Arrays.toString(fields));
		for (String fieldAndType : fields) {
			String IDPField = "";
			String IDPFieldType = "";
			
			try {
				String[] fieldAndTypeArray = fieldAndType.trim().split("\\s");
				IDPField =	fieldAndTypeArray[0].trim();
				IDPFieldType = fieldAndTypeArray[fieldAndTypeArray.length-1].trim();;
//				System.err.println(Arrays.toString(fieldAndType.trim().split("\\s")));
			} catch (Exception e) {
				System.err.println("field prase fail:"+IDPField+","+IDPFieldType);
			}
			if(!IDPField.trim().equals("")){
				StringBuffer objectName = new StringBuffer();
				String SFDCField = getFieldNameInSalesforce(IDPField,tableName,objectName);
				
				 tableName2ObjectName.put(tableName,objectName.toString() );
				 tableName2ObjectName.put(objectName.toString(),tableName);
				 initMap(IDPFields,tableName);
				 initMap(IDPFieldTypes,tableName);
				 initMap(SFDCFields,objectName.toString());
				 
				  IDPFields.get(tableName).add(IDPField.trim());
				   SFDCFields.get(tableName2ObjectName.get(tableName)).add(SFDCField);	
				   IDPFieldTypes.get(tableName).add(IDPFieldType);
			}
		}
		
		
		}
	private void initMap(HashMap<String, ArrayList<String>> map,
			String key) {
		if(!map.containsKey(key)){
			map.put(key, new ArrayList<String>());
		}
	}
	
	private String getFieldNameInSalesforce(String fieldNameInDB,
			String tableName, StringBuffer objectName) {
		if(fieldNameInDB.trim().contains("CreatedBy")){
			System.out.println();
		}
		//1.1获取salesforce数据结构 
        HashSet<String>    filedsInSalesforce = getFieldNamesFromSalesForceByIndistinctTableName(tableName,objectName);
		for (String filedInSalesforce : filedsInSalesforce) {
		
			if(filedInSalesforce.trim().equalsIgnoreCase(fieldNameInDB)){
				return filedInSalesforce.trim();
			}
			if(filedInSalesforce.trim().equalsIgnoreCase(fieldNameInDB+"__c")){
				return filedInSalesforce.trim();
			}
			if(filedInSalesforce.trim().equalsIgnoreCase(fieldNameInDB+"id")){
				return filedInSalesforce.trim();
			}
		}
		return fieldNameInDB+"无匹配字段";
	}
	public static HashSet<String> getFieldNamesFromSalesForceByIndistinctTableName(String tableName, StringBuffer objectName){
		    String objectNameString = tableName.replace("dbo.SFDC_", "").replace("SFDC_", "").replace("_Temp", "").trim();
	  HashSet<String>   resultSet  = Helper.getFieldNamesFromSalesForceByObjectName(objectNameString);
	  if(resultSet==null || resultSet.size() < 1){
		  objectNameString = objectNameString+"__c";
		  resultSet = Helper.getFieldNamesFromSalesForceByObjectName(objectNameString);
	  }
	  if(resultSet==null || resultSet.size() < 1){
		  System.err.println("can't find object named "+objectNameString);
	  }else{
		  objectName.append(objectNameString);
	  }
    return   resultSet;
}
	
	
}