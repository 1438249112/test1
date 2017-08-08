package SFDC2IDP.BASESFDCEXCEL.MAPPINGHANDLER;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import SFDC2IDP.BASE.COMMON.CONSTANTS;
import SFDC2IDP.BASE.COMMON.CreateSQLFileSolver;
import SFDC2IDP.BASE.COMMON.Helper;
import SFDC2IDP.BASE.INTERFACE.IMappingHandler;
import SFDC2IDP.BASE.INTERFACE.IMappingSolver;
import SFDC2IDP.BASE.SegmentMaker.DssSegmentMaker;
import SFDC2IDP.BASE.SegmentMaker.InputSechmaSegmentMaker;
import SFDC2IDP.BASESFDCEXCEL.MAPPINGFILESOLVER.MappingExeclFileSolver;
import edu.emory.mathcs.backport.java.util.Arrays;

public class Handle_Mapping_BaseExcel_1 implements IMappingHandler {

	private static HashMap<String, IMappingHandler> instances = new HashMap<String, IMappingHandler>();

	// public static IMappingHandler getInstance(){
	// return instance;
	//
	// }
	public static IMappingHandler getInstance(Boolean checkFromFields,
			Boolean checkToFileds) {
		checkFromFields = checkFromFields == null ? false : checkFromFields;
		checkToFileds = checkToFileds == null ? false : checkToFileds;
		String key = checkFromFields.toString() + checkToFileds.toString();
		if (!instances.containsKey(key)) {
			instances.put(key, new Handle_Mapping_BaseExcel_1(checkFromFields,
					checkToFileds));
		}
		return instances.get(key).execute();
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	private HashMap<String/** table_name **/
	, String> sqlMap = new HashMap<String, String>();
	private HashMap<String/** object_name **/
	, String> soqlMap = new HashMap<String, String>();
	private HashMap<String/** object_name **/
	, String> mappingFraction = new HashMap<String, String>();

	private HashMap<String/** object_name **/
	, String> mapping_in_schema_Fraction = new HashMap<String, String>();
	private HashMap<String/** object_name **/
	, String> mapping_out_schema_Fraction = new HashMap<String, String>();

	private Boolean checkFromFields = true;
	private Boolean checkToFields = true;
	private DssSegmentMaker dssSegmentMaker = new DssSegmentMaker();
	private InputSechmaSegmentMaker inputSechmaSegmentMaker = new InputSechmaSegmentMaker();
	
	private ArrayList<String> tableNames = new ArrayList<String>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * SFDC2IDP.BASESFDCEXCEL.MAPPINGHANDLER.IMappingHandler#getMappingFraction
	 * ()
	 */
	@Override
	public HashMap<String, String> getMappingFraction() {
		return mappingFraction;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * SFDC2IDP.BASESFDCEXCEL.MAPPINGHANDLER.IMappingHandler#setMappingFraction
	 * (java.util.HashMap)
	 */
	@Override
	public void setMappingFraction(HashMap<String, String> mappingFraction) {
		this.mappingFraction = mappingFraction;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * SFDC2IDP.BASESFDCEXCEL.MAPPINGHANDLER.IMappingHandler#getSqlFileSolver()
	 */
	@Override
	public CreateSQLFileSolver getSqlFileSolver() {
		return sqlFileSolver;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * SFDC2IDP.BASESFDCEXCEL.MAPPINGHANDLER.IMappingHandler#setSqlFileSolver
	 * (SFDC2IDP.BASE.COMMON.CreateSQLFileSolver)
	 */
	@Override
	public void setSqlFileSolver(CreateSQLFileSolver sqlFileSolver) {
		this.sqlFileSolver = sqlFileSolver;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * SFDC2IDP.BASESFDCEXCEL.MAPPINGHANDLER.IMappingHandler#getObjectNames()
	 */
	@Override
	public HashSet<String> getObjectNames() {
		return objectNames;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * SFDC2IDP.BASESFDCEXCEL.MAPPINGHANDLER.IMappingHandler#setObjectNames(
	 * java.util.HashSet)
	 */
	@Override
	public void setObjectNames(HashSet<String> objectNames) {
		this.objectNames = objectNames;
	}

	private CreateSQLFileSolver sqlFileSolver = new CreateSQLFileSolver();
	private HashMap<String, String> tableName2ObjectName = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see SFDC2IDP.BASESFDCEXCEL.MAPPINGHANDLER.IMappingHandler#getSqlMap()
	 */
	@Override
	public HashMap<String, String> getSqlMap() {
		return sqlMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * SFDC2IDP.BASESFDCEXCEL.MAPPINGHANDLER.IMappingHandler#setSqlMap(java.
	 * util.HashMap)
	 */
	@Override
	public void setSqlMap(HashMap<String, String> sqlMap) {
		this.sqlMap = sqlMap;
	}

	public Handle_Mapping_BaseExcel_1 execute() {
		try {

			MappingExeclFileSolver FileSolver = new MappingExeclFileSolver();
			HashMap<String, ArrayList<String>> IDPFields = FileSolver
					.getIDPFields();
			HashMap<String, ArrayList<String>> SfdcObjectNameS = FileSolver.getSfdcObjectNameS();
			HashMap<String, ArrayList<String>> SFDCFields = FileSolver
					.getSFDCFields();
			HashMap<String, ArrayList<String>> IDPFieldTypes = FileSolver
					.getIDPFieldTypes();
			ArrayList<String> solverTableNames = FileSolver.getTableNames();
			tableName2ObjectName = FileSolver.getTableName2ObjectName();
		
			for (String tableName : solverTableNames) {
				try {
					boolean dealed = false;
					boolean hasCorrectPrase = true;
					String objectName = tableName2ObjectName.get(tableName);
					System.out.println("objectName"+objectName);
					// 1.idp params
					ArrayList<String> idpFileds = IDPFields.get(tableName);
					ArrayList<String> idpFieldTypes = IDPFieldTypes
							.get(tableName);
					ArrayList<String> sfdcFileds = SFDCFields.get(objectName);
					
					ArrayList<String> SfdcobjectnameS = SfdcObjectNameS.get(objectName);
					checkidpFiledIfExistInTABLE(idpFieldTypes, tableName);
					String valuesString = "";
					String filedsString = "";
					// 2.soql params
					String soql = "select ";
					// 3.mappingFraction
					String mappingFractionString = "";
					// 5.mapping_out_schema_Fraction
					String mapping_out_schema_FractionString = "";

					boolean handledInputtime = false;
					for (int i = 0; i < idpFileds.size(); i++) {
						String sfdcFiled = sfdcFileds.get(i).trim();
						String idpFiled = idpFileds.get(i).trim();
						String Sfdcobjectname = SfdcobjectnameS.get(i).trim();
						if(!isInThisObject(Sfdcobjectname,objectName)){
							sfdcFiled = Sfdcobjectname +"."+sfdcFiled;
						}
						// after all ; check duplicate
						if (!salesforceObject.containsKey(objectName)) {
							salesforceObject.put(objectName, new ArrayList());
						}
						if (salesforceObject.get(objectName).contains(
								sfdcFiled.toLowerCase())) {
							System.err.println("\""+sfdcFiled + "\" of "
									+ objectName + " In Mapping Excel duplicate!");
							continue;
						} else {
							salesforceObject.get(objectName).add(
									sfdcFiled.toLowerCase());
						}

						// 1.idp

						idpFiled = idpFiled.trim();
						if (this.checkToFields) {
							hasCorrectPrase &= checkidpFiledIfExistInIDP(
									tableName, idpFiled);

						}
						if (!idpFileds.contains(CONSTANTS.mapping_batch_flag) && i == 0) {
							valuesString += ":" + idpFiled + ",:"+CONSTANTS.mapping_batch_flag+",";
							filedsString += idpFiled + ","+CONSTANTS.mapping_batch_flag+",";
						} else {
							valuesString += ":" + idpFiled + ",";
							filedsString += idpFiled + ",";
						}

						// 2.soql

						if (sfdcFiled != null && !sfdcFiled.trim().equals("")) {
							sfdcFiled = sfdcFiled.trim();
							if (this.checkFromFields) {

								hasCorrectPrase &= checkSoqlFiledIfExistInSalesforce(
										objectName, sfdcFiled);
							}
							if (!sfdcFiled.equalsIgnoreCase("")) {
								soql += sfdcFiled + ",";
							}
						}

						// 3.mappingFraction
						String idpFieldType = idpFieldTypes.get(i);
//						String sfdfFieldType = idpFieldTypes.get(i);
						// datetime_formate;decimal_fromate;int_fromate;
						if (true) {
							String filedInfo = "\"[fieldName="+sfdcFiled+",fieldType="+idpFieldType+"]\"";
							if (idpFieldType.trim().toLowerCase()
									.contains("date")) {
								mappingFractionString += addMappingFunction(idpFiled,sfdcFiled,CONSTANTS.mapping_function_datetime_formate,filedInfo);

							} else if (idpFieldType.trim().toLowerCase()
									.contains("int")) {
								mappingFractionString += addMappingFunction(idpFiled,sfdcFiled,CONSTANTS.mapping_function_int_fromate,filedInfo);

							} else if (idpFieldType.trim().toLowerCase()
									.contains("decimal")) {
								mappingFractionString += addMappingFunction(idpFiled,sfdcFiled,CONSTANTS.mapping_function_decimal_fromate,filedInfo);

							} else if (idpFieldType.trim().toLowerCase()
									.contains("decimail")) {
								
								if(!dealed){
								    System.err.println("need changed mapper table name = "+tableName);
								}
									dealed = true;
									mappingFractionString += addMappingFunction(idpFiled,sfdcFiled,CONSTANTS.mapping_function_decimal_fromate,filedInfo);

							}else if (idpFiled.trim().toLowerCase()
									.contains(CONSTANTS.mapping_batch_flag)) {
								handledInputtime = true;
								mappingFractionString += CONSTANTS.outputObjectSuffix
										+ CONSTANTS.mapping_batch_flag+" = "
										+ CONSTANTS.mapping_var_date + ";";

							} else {
								mappingFractionString += CONSTANTS.outputObjectSuffix
										+ idpFiled.toLowerCase()
										+ " = "
										+ CONSTANTS.inputObjectSuffix
										+ sfdcFiled + ";";
							}

						}
						// 4.mapping_in_schema_Fraction
						
						inputSechmaSegmentMaker.makeSegment(objectName, idpFiled,
								idpFieldType, sfdcFiled,
								i == idpFileds.size() - 1);
						// 5.mapping_out_schema_Fraction
						mapping_out_schema_FractionString += CONSTANTS.out_schema_fraction_template
								.replace("{keyWord}", idpFiled.toLowerCase());
						// 6.dssSegmentMaker
						dssSegmentMaker.makeSegment(tableName, idpFiled,
								idpFieldType, null,
								i == idpFileds.size() - 1);
					}
					// 1.idp deal with
					valuesString = "("
							+ valuesString.substring(0,
									valuesString.length() - 1) + ")";
					filedsString = "("
							+ filedsString.substring(0,
									filedsString.length() - 1) + ")";

					String insertSql = "insert into " + tableName + "_Temp "
							+ filedsString + " values " + valuesString;
					String sqlXml = "<Insert2" + tableName + "_Temp>"
							+ insertSql + "<Insert2" + tableName + "_Temp/>";
					sqlMap.put(tableName, sqlXml.toLowerCase());
					dssSegmentMaker.replaceSqlContent("Insert2" + tableName
							+ "_Temp", insertSql.trim().toLowerCase());
					// 2.soql deal with

					soql = soql.substring(0, soql.length() - 1);
					soql += " from " + objectName;
					soqlMap.put(objectName, soql);
					// 3.mappingFraction
					if (!handledInputtime) {
						mappingFractionString += CONSTANTS.outputObjectSuffix
								+ CONSTANTS.mapping_batch_flag+" = " + CONSTANTS.mapping_var_date
								+ ";";
					}
					mappingFraction.put(objectName, mappingFractionString);
					// 5.mapping_out_schema_Fraction
					if (!handledInputtime) {
						mapping_out_schema_FractionString += CONSTANTS.out_schema_fraction_template
								.replace("{keyWord}", CONSTANTS.mapping_batch_flag);
					}
					mapping_out_schema_Fraction.put(objectName,
							mapping_out_schema_FractionString
									.substring(0,
											mapping_out_schema_FractionString
													.length() - 1));
					if (hasCorrectPrase) {
						tableNames.add(tableName);
					}
				} catch (Exception e) {
					if (e.getMessage() != null && !e.getMessage().equals("")) {
						e.printStackTrace();
						System.err.println(e.getMessage());
					} else {
						System.err.println("解析 table " + tableName + " 出现异常");
					}

				}

			}

			String proxyNames = "";
			for (String tableName : tableNames) {
				proxyNames += "http://elb.wso2.lenovo.com:8080/esb/services/SFDC2TELE_" + tableName.replace("SFDC_", "")
						+ ",";

			}
			String clearSqls = "";
			for (String tableName : tableNames) {
				clearSqls += "delete from " + tableName+"_TEMP;";

			}
			String clearPROXYs = "";
			for (String tableName : tableNames) {
				clearPROXYs += "mc.getConfiguration().getRegistry().updateResource"
						+ "(\"gov:/repository/services/SFDC2TELE_Account/variables/query.global.latestdate\",\"0\");"
						.replace("Account", tableName.replace("SFDC_", ""));

			}
			String selectAllSqls = "";
			for (String tableName : tableNames) {
				selectAllSqls += "select count(*) "+ tableName+"_TEMP from " + tableName+"_TEMP;";

			}
			
			
			String result ="";
			for (String string : dssSegmentMaker.getResults().values()) {
				result+=string;
			}
			System.err
			.println(result);
			System.err
					.println("Totally Success URL :" + proxyNames);
			System.err
			.println("clear Sql : " + clearSqls);
			System.err
			.println("select Sql : " + selectAllSqls);

			System.err
			.println("clear prxoy : " + clearPROXYs);

		} catch (Exception e) {
			System.err.println("处理excel解析后的数据出错");
			e.printStackTrace();
		}
		return this;
	}

	private String addMappingFunction(String fromField,String toFiled,String function,String msg ) {
		
		return  CONSTANTS.outputObjectSuffix
				+ fromField.toLowerCase()
				+ " ="
				+ function
				+ "("
				+ CONSTANTS.inputObjectSuffix
				+ toFiled + ","
				+ CONSTANTS.inputObjectSuffix
				+ "Id" +","+msg+");";
	}

	private boolean isInThisObject(String sfdcobjectname, String objectName) {

       if(sfdcobjectname.trim().equalsIgnoreCase(objectName.trim())||sfdcobjectname==null||sfdcobjectname.trim().equalsIgnoreCase("")){
    	   return true;
       }
       System.err.println("sfdcobjectname = "+sfdcobjectname+" &&&&&&& objectName = "+objectName);
		return false;
	}

	public Handle_Mapping_BaseExcel_1(Boolean checkFromFields,
			Boolean checkToFileds) {
		this.checkFromFields = checkFromFields;
		this.checkToFields = checkToFileds;

	}

	private void checkidpFiledIfExistInTABLE(ArrayList<String> idpFieldTypes,
			String tableName) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see SFDC2IDP.BASESFDCEXCEL.MAPPINGHANDLER.IMappingHandler#
	 * getMapping_in_schema_Fraction()
	 */
	@Override
	public HashMap<String, String> getMapping_in_schema_Fraction() {
		return inputSechmaSegmentMaker.getResults();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see SFDC2IDP.BASESFDCEXCEL.MAPPINGHANDLER.IMappingHandler#
	 * setMapping_in_schema_Fraction(java.util.HashMap)
	 */
	@Override
	public void setMapping_in_schema_Fraction(
			HashMap<String, String> mapping_in_schema_Fraction) {
		this.mapping_in_schema_Fraction = mapping_in_schema_Fraction;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see SFDC2IDP.BASESFDCEXCEL.MAPPINGHANDLER.IMappingHandler#
	 * getMapping_out_schema_Fraction()
	 */
	@Override
	public HashMap<String, String> getMapping_out_schema_Fraction() {
		return mapping_out_schema_Fraction;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see SFDC2IDP.BASESFDCEXCEL.MAPPINGHANDLER.IMappingHandler#
	 * setMapping_out_schema_Fraction(java.util.HashMap)
	 */
	@Override
	public void setMapping_out_schema_Fraction(
			HashMap<String, String> mapping_out_schema_Fraction) {
		this.mapping_out_schema_Fraction = mapping_out_schema_Fraction;
	}

	private boolean checkidpFiledIfExistInIDP(String tableName, String filed) {
	
		if(filed.contains(".")){
			String  objectName = filed.substring(0,filed.indexOf("."));
			tableName = tableName2ObjectName.get(objectName);
			filed = filed.substring(filed.indexOf(".")+1);
		}
		HashMap<String, HashSet<String>> tables = sqlFileSolver
				.getIDPFieldsLowCase();
		HashSet<String> fields = tables.get((tableName + "_Temp").trim()
				.toLowerCase());
		try {
			if (fields.contains(filed.trim().toLowerCase())) {
				return true;
			}
		} catch (Exception e) {
		}
		System.err.println(filed + " doesn't exist in Table named " + tableName
				+ maybeName(filed, fields));
		return false;
	}

	private HashSet<String> objectNames = new HashSet<String>();
	private HashMap<String, ArrayList> salesforceObject = new HashMap<String, ArrayList>();

	private boolean checkSoqlFiledIfExistInSalesforce(String objectName,
			String filed) throws Exception {
		if(filed.contains(".")){
			objectName = filed.substring(0,filed.indexOf("."));
			filed = filed.substring(filed.indexOf(".")+1);
		}
		filed = filed.trim();

		HashSet<String> fileds = Helper
				.getFieldNamesFromSalesForceByObjectName(objectName);
		try {

			if (fileds == null || fileds.isEmpty() || fileds.size() < 3) {
				String error = objectName + " isn't Salesforce Object .";
				throw new Exception(error);
			}

			if (fileds.contains(filed.trim())) {
				return true;
			}
		} catch (Exception e) {
			if (e.getMessage().toLowerCase().contains("salesforce")) {
				throw new Exception(e.getMessage());
			} else {
				e.printStackTrace();
			}
		}

		System.err.println(filed + " doesn't exist in Object named "
				+ objectName + maybeName(filed, fileds));
		return false;
	}

	private String maybeName(String filed, HashSet<String> fileds) {
		String maybeFileds = "; maybe the filed name is ";
		boolean cannotFind = true;
		for (String f : fileds) {
			if (f.trim().toLowerCase().contains(filed.trim().toLowerCase())
					|| filed.trim().toLowerCase()
							.contains(f.trim().toLowerCase())) {
				maybeFileds += f + ",";
				cannotFind = false;
			}
		}
		if (cannotFind) {
			return "";
		}
		return maybeFileds;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see SFDC2IDP.BASESFDCEXCEL.MAPPINGHANDLER.IMappingHandler#getSoqlMap()
	 */
	@Override
	public HashMap<String, String> getSoqlMap() {
		return soqlMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * SFDC2IDP.BASESFDCEXCEL.MAPPINGHANDLER.IMappingHandler#setSoqlMap(java
	 * .util.HashMap)
	 */
	@Override
	public void setSoqlMap(HashMap<String, String> soqlMap) {
		this.soqlMap = soqlMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * SFDC2IDP.BASESFDCEXCEL.MAPPINGHANDLER.IMappingHandler#getTableNames()
	 */
	@Override
	public ArrayList<String> getTableNames() {
		return tableNames;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * SFDC2IDP.BASESFDCEXCEL.MAPPINGHANDLER.IMappingHandler#setTableNames(java
	 * .util.ArrayList)
	 */
	@Override
	public void setTableNames(ArrayList<String> tableNames) {
		this.tableNames = tableNames;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * SFDC2IDP.BASESFDCEXCEL.MAPPINGHANDLER.IMappingHandler#getTableName2ObjectName
	 * ()
	 */
	@Override
	public HashMap<String, String> getTableName2ObjectName() {
		return tableName2ObjectName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * SFDC2IDP.BASESFDCEXCEL.MAPPINGHANDLER.IMappingHandler#setTableName2ObjectName
	 * (java.util.HashMap)
	 */
	@Override
	public void setTableName2ObjectName(
			HashMap<String, String> tableName2ObjectName) {
		this.tableName2ObjectName = tableName2ObjectName;
	}

}