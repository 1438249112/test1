package SFDC2IDP.BASEIDPSQL.MAPPINGHANDLER;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import SFDC2IDP.BASE.COMMON.CreateSQLFileSolver;
import SFDC2IDP.BASE.COMMON.Helper;
import SFDC2IDP.BASE.INTERFACE.IMappingHandler;
import SFDC2IDP.BASE.INTERFACE.IMappingSolver;
import SFDC2IDP.BASESFDCEXCEL.MAPPINGFILESOLVER.MappingExeclFileSolver;

public class Handle_Mapping_BaseSQL implements IMappingHandler {
	 

		public static IMappingHandler instance = new Handle_Mapping_BaseSQL();
	
    public static IMappingHandler getInstance(){
		return instance;
    	
    }
	/**
	 * @param args
	 * @throws Exception
	 */
  private HashMap<String/**table_name**/, String> sqlMap = new HashMap<String, String> ();
  private HashMap<String/**object_name**/, String> soqlMap = new HashMap<String, String> ();
  private HashMap<String/**object_name**/, String> mappingFraction = new HashMap<String, String> ();
  
  private HashMap<String/**object_name**/, String> mapping_in_schema_Fraction = new HashMap<String, String> ();
  private HashMap<String/**object_name**/, String> mapping_out_schema_Fraction = new HashMap<String, String> ();

	private ArrayList<String> tableNames = new  ArrayList<String>();
	/* (non-Javadoc)
	 * @see SFDC2IDP.BASESFDCEXCEL.MAPPINGHANDLER.IMappingHandler#getMappingFraction()
	 */
	@Override
	public HashMap<String, String> getMappingFraction() {
		return mappingFraction;
	}

	/* (non-Javadoc)
	 * @see SFDC2IDP.BASESFDCEXCEL.MAPPINGHANDLER.IMappingHandler#setMappingFraction(java.util.HashMap)
	 */
	@Override
	public void setMappingFraction(HashMap<String, String> mappingFraction) {
		this.mappingFraction = mappingFraction;
	}

	/* (non-Javadoc)
	 * @see SFDC2IDP.BASESFDCEXCEL.MAPPINGHANDLER.IMappingHandler#getSqlFileSolver()
	 */
	@Override
	public CreateSQLFileSolver getSqlFileSolver() {
		return sqlFileSolver;
	}

	/* (non-Javadoc)
	 * @see SFDC2IDP.BASESFDCEXCEL.MAPPINGHANDLER.IMappingHandler#setSqlFileSolver(SFDC2IDP.BASE.COMMON.CreateSQLFileSolver)
	 */
	@Override
	public void setSqlFileSolver(CreateSQLFileSolver sqlFileSolver) {
		this.sqlFileSolver = sqlFileSolver;
	}

	/* (non-Javadoc)
	 * @see SFDC2IDP.BASESFDCEXCEL.MAPPINGHANDLER.IMappingHandler#getObjectNames()
	 */
	@Override
	public HashSet<String> getObjectNames() {
		return objectNames;
	}

	/* (non-Javadoc)
	 * @see SFDC2IDP.BASESFDCEXCEL.MAPPINGHANDLER.IMappingHandler#setObjectNames(java.util.HashSet)
	 */
	@Override
	public void setObjectNames(HashSet<String> objectNames) {
		this.objectNames = objectNames;
	}
	private CreateSQLFileSolver sqlFileSolver = new CreateSQLFileSolver();
	private HashMap<String,String> tableName2ObjectName = null;
	/* (non-Javadoc)
	 * @see SFDC2IDP.BASESFDCEXCEL.MAPPINGHANDLER.IMappingHandler#getSqlMap()
	 */
	@Override
	public HashMap<String, String> getSqlMap() {
		return sqlMap;
	}

	/* (non-Javadoc)
	 * @see SFDC2IDP.BASESFDCEXCEL.MAPPINGHANDLER.IMappingHandler#setSqlMap(java.util.HashMap)
	 */
	@Override
	public void setSqlMap(HashMap<String, String> sqlMap) {
		this.sqlMap = sqlMap;
	}

	private Handle_Mapping_BaseSQL() {try {

		IMappingSolver FileSolver = new MappingExeclFileSolver();
		HashMap<String, ArrayList<String>> IDPFields = FileSolver.getIDPFields();
		HashMap<String, ArrayList<String>> SFDCFields = FileSolver.getSFDCFields();
		HashMap<String, ArrayList<String>> IDPFieldTypes = FileSolver.getIDPFieldTypes();
		 ArrayList<String> solverTableNames = FileSolver.getTableNames();
		 tableName2ObjectName  = FileSolver.getTableName2ObjectName();
		 boolean hasCorrectPrase = true;
	for (String tableName : solverTableNames) {
		try {
			
			String objectName = tableName2ObjectName.get(tableName);
			//1.idp params
		ArrayList<String> idpFileds = IDPFields.get(tableName);
		ArrayList<String> idpFieldTypes  = IDPFieldTypes.get(tableName);
		ArrayList<String> sfdcFileds  = SFDCFields.get(objectName);
		 String valuesString = "";
         String filedsString = "";
         //2.soql params
     	String soql = "select ";
     	//3.mappingFraction
		String mappingFractionString = "";
		//4.mapping_in_schema_Fraction
		String mapping_in_schema_FractionString = "";
		//5.mapping_out_schema_Fraction
		String mapping_out_schema_FractionString = "";

		for (int i = 0; i < idpFileds.size(); i++) {
			    //1.idp
			    String idpFiled = idpFileds.get(i);			
			     idpFiled = idpFiled.trim();
//				hasCorrectPrase &= checkidpFiledIfExistInIDP(tableName,idpFiled);

				valuesString +=":"+idpFiled+",";
				filedsString +=idpFiled+",";
			   //2.soql
				String sfdcFiled = sfdcFileds.get(i);
				if(sfdcFiled!=null&&!sfdcFiled.trim().equals("")){
					sfdcFiled = sfdcFiled.trim();
//					hasCorrectPrase &= checkSoqlFiledIfExistInSalesforce(objectName,sfdcFiled);
					 if(!sfdcFiled.equalsIgnoreCase("")){
						 soql += sfdcFiled + ",";
					 }
				}
				
				//3.mappingFraction
//				outputsoapenv_Envelope.soapenv_Body.dat_Insert2SFDC_Account_Temp_batch_req.dat_Insert2SFDC_Account_Temp[count_i_records].dat_id = inputjsonObject.records[i_records].Id;
//				outputsoapenv_Envelope.soapenv_Body.dat_Insert2SFDC_Account_Temp_batch_req.dat_Insert2SFDC_Account_Temp[count_i_records].dat_lastmodifieddate = data_formate(inputjsonObject.records[i_records].LastModifiedDate);
//				outputsoapenv_Envelope.soapenv_Body.dat_Insert2SFDC_Account_Temp_batch_req.dat_Insert2SFDC_Account_Temp[count_i_records].dat_createddate = data_formate(inputjsonObject.records[i_records].CreatedDate);
				String idpFieldType = idpFieldTypes.get(i);
				if(hasCorrectPrase){
					String target = "outputresult.insert2sfdc_account_temp_batch_req.insert2sfdc_account_temp[count_i_records].{idpFiled}=";
					String from = "inputjsonObject.records[i_records].{sfdcFiled}";
					if(idpFieldType.trim().equalsIgnoreCase("DateTime")){
						mappingFractionString+=target.replace("{idpFiled}", idpFiled.toLowerCase())
								+ "data_formate("
								+ from.replace("{sfdcFiled}", sfdcFiled)
								+ ");";

					}else{
						mappingFractionString+=target.replace("{idpFiled}", idpFiled.toLowerCase())
								+ from.replace("{sfdcFiled}", sfdcFiled)
								+ ";";
					}
				}
				
				//4.mapping_in_schema_Fraction
				mapping_in_schema_FractionString+=("\"Id\" : { \"id\" : \"http://wso2jsonschema.org/records/Id\", \"type\" : \"string\" },").replace("Id", sfdcFiled);
				//5.mapping_out_schema_Fraction
				mapping_out_schema_FractionString+=("\"{id}\" : { \"id\" : "
						+ "\"http://wso2jsonschema.org/insert2sfdc_account_temp_batch_req/insert2sfdc_account_temp/0/{id}\""
						+ ", \"type\" : \"string\" },").toLowerCase().replace("{id}", idpFiled.toLowerCase());
			
		}
			//1.idp deal with 
				valuesString ="("+ valuesString.substring(0, valuesString.length()-1)+")";
				filedsString ="("+  filedsString.substring(0, filedsString.length()-1)+")";

				
			String insertSql = "insert into "+tableName+"_Temp "+ filedsString +" values " + valuesString ;
			String sqlXml = "<Insert2"+tableName+"_Temp>" + insertSql + "<Insert2"+tableName+"_Temp/>" ;
			sqlMap.put(tableName,sqlXml.toLowerCase());
			//2.soql deal with 
			
			soql = soql.substring(0,soql.length()-1);
			soql +=" from " + objectName;
			soqlMap.put(objectName, soql);
			//3.mappingFraction
			mappingFraction.put(objectName, mappingFractionString);
			//4.mapping_in_schema_Fraction
			mapping_in_schema_Fraction.put(objectName,  mapping_in_schema_FractionString.substring(0, mapping_in_schema_FractionString.length()-1));
			//5.mapping_out_schema_Fraction
			mapping_out_schema_Fraction.put(objectName,  mapping_out_schema_FractionString.substring(0, mapping_out_schema_FractionString.length()-1));
			if(hasCorrectPrase){
				tableNames.add(tableName);
			}
		} catch (Exception e) {
			if(e.getMessage()!=null && !e.getMessage().equals("")){
				System.err.println(e.getMessage());
			}else{
				System.err.println("���� table "+tableName +" �����쳣");
			}
			
		}
	}
	
	} catch (Exception e) {
		System.err.println("����excel����������ݳ���");
	e.printStackTrace();
	}}
    /* (non-Javadoc)
	 * @see SFDC2IDP.BASESFDCEXCEL.MAPPINGHANDLER.IMappingHandler#getMapping_in_schema_Fraction()
	 */
    @Override
	public HashMap<String, String> getMapping_in_schema_Fraction() {
		return mapping_in_schema_Fraction;
	}

	/* (non-Javadoc)
	 * @see SFDC2IDP.BASESFDCEXCEL.MAPPINGHANDLER.IMappingHandler#setMapping_in_schema_Fraction(java.util.HashMap)
	 */
	@Override
	public void setMapping_in_schema_Fraction(
			HashMap<String, String> mapping_in_schema_Fraction) {
		this.mapping_in_schema_Fraction = mapping_in_schema_Fraction;
	}

	/* (non-Javadoc)
	 * @see SFDC2IDP.BASESFDCEXCEL.MAPPINGHANDLER.IMappingHandler#getMapping_out_schema_Fraction()
	 */
	@Override
	public HashMap<String, String> getMapping_out_schema_Fraction() {
		return mapping_out_schema_Fraction;
	}

	/* (non-Javadoc)
	 * @see SFDC2IDP.BASESFDCEXCEL.MAPPINGHANDLER.IMappingHandler#setMapping_out_schema_Fraction(java.util.HashMap)
	 */
	@Override
	public void setMapping_out_schema_Fraction(
			HashMap<String, String> mapping_out_schema_Fraction) {
		this.mapping_out_schema_Fraction = mapping_out_schema_Fraction;
	}

	private boolean checkidpFiledIfExistInIDP(String tableName, String filed) {
    	try {
        	HashMap<String, HashSet<String>>  tables = sqlFileSolver.getIDPFieldsLowCase();
            HashSet<String>	fields = tables.get((tableName+"_Temp").trim().toLowerCase());
            if(fields.contains(filed.trim().toLowerCase())){
            	return true;
            }
		} catch (Exception e) {
		}
        System.err.println(filed+" doesn't exist in Table named " + tableName );
		return false;
	}
	private  HashSet<String> objectNames = new  HashSet<String>();
	private boolean checkSoqlFiledIfExistInSalesforce(String objectName,
			String filed) throws Exception {
		HashSet<String>  fileds = Helper.getFieldNamesFromSalesForceByObjectName(objectName);
		try {
			
			if(fileds==null || fileds.isEmpty() || fileds.size() < 3){
				String error = objectName+" isn't Salesforce Object .";
				  throw new Exception(error);
			}
		
			 if(fileds.contains(filed.trim())){
		    	  return true;
		      }
		} catch (Exception e) {
			if(e.getMessage().toLowerCase().contains("salesforce")){
				throw new Exception(e.getMessage());
			}else{
				e.printStackTrace();
			}
		}
     
        System.err.println(filed+" doesn't exist in Object named " + objectName+maybeName(filed,fileds));
		return false;
	}

	private String maybeName(String filed, HashSet<String> fileds) {
		String maybeFileds = "; maybe the filed name is ";
		boolean cannotFind = true;
		for (String f : fileds) {
			if(f.trim().toLowerCase().contains(filed.trim().toLowerCase())){
				maybeFileds +=f+ ",";
				cannotFind = false;
			}
		}
		if(cannotFind){
			return "";
		}
		return maybeFileds;
	}

	/* (non-Javadoc)
	 * @see SFDC2IDP.BASESFDCEXCEL.MAPPINGHANDLER.IMappingHandler#getSoqlMap()
	 */
	@Override
	public HashMap<String, String> getSoqlMap() {
		return soqlMap;
	}

	/* (non-Javadoc)
	 * @see SFDC2IDP.BASESFDCEXCEL.MAPPINGHANDLER.IMappingHandler#setSoqlMap(java.util.HashMap)
	 */
	@Override
	public void setSoqlMap(HashMap<String, String> soqlMap) {
		this.soqlMap = soqlMap;
	}

	/* (non-Javadoc)
	 * @see SFDC2IDP.BASESFDCEXCEL.MAPPINGHANDLER.IMappingHandler#getTableNames()
	 */
	@Override
	public ArrayList<String> getTableNames() {
		return tableNames;
	}

	/* (non-Javadoc)
	 * @see SFDC2IDP.BASESFDCEXCEL.MAPPINGHANDLER.IMappingHandler#setTableNames(java.util.ArrayList)
	 */
	@Override
	public void setTableNames(ArrayList<String> tableNames) {
		this.tableNames = tableNames;
	}

	/* (non-Javadoc)
	 * @see SFDC2IDP.BASESFDCEXCEL.MAPPINGHANDLER.IMappingHandler#getTableName2ObjectName()
	 */
	@Override
	public HashMap<String, String> getTableName2ObjectName() {
		return tableName2ObjectName;
	}

	/* (non-Javadoc)
	 * @see SFDC2IDP.BASESFDCEXCEL.MAPPINGHANDLER.IMappingHandler#setTableName2ObjectName(java.util.HashMap)
	 */
	@Override
	public void setTableName2ObjectName(HashMap<String, String> tableName2ObjectName) {
		this.tableName2ObjectName = tableName2ObjectName;
	}

	@Override
	public IMappingHandler execute() {
		// TODO Auto-generated method stub
		return null;
	}
	

}