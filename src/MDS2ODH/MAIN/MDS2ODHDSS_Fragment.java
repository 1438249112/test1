package MDS2ODH.MAIN;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import edu.emory.mathcs.backport.java.util.Arrays;
import SFDC2IDP.BASE.COMMON.CONSTANTS;
import SFDC2IDP.BASE.COMMON.Helper;
import SFDC2IDP.BASE.INTERFACE.IMappingSolver;

public class MDS2ODHDSS_Fragment implements IMappingSolver {
	public static void main(String[] args) throws Exception {
		new MDS2ODHDSS_Fragment().generateSql();
	}

	public void generateSql() {
		HashMap<String, ArrayList<String>> idps = this.getIDPFields();
		HashMap<String, ArrayList<String>> idpTypes = this.getIDPFieldTypes();
		for (Entry<String, ArrayList<String>> odhforms : idps.entrySet()) {
			String tableName = odhforms.getKey();
			String xmlFragment = " ";
			String maynullInt = "<xsl:if test=\"normalize-space({id})!=''\"> <{id}> <xsl:value-of  select=\"{id}\" /> </{id}> </xsl:if>";
			String maynullTimeStamp = "<xsl:if test=\"normalize-space({id})!=''\"> <{id}> <xsl:value-of  select=\"translate(normalize-space({id}),' ','T')\" /> </{id}> </xsl:if>";

			
			String cannotnull = " <{id}> <xsl:value-of  select=\"{id}\" /> </{id}>";
			String  sys_created_date = "<sys_created_date> <xsl:value-of select=\"translate(normalize-space($sys_created_date),' ','T')\" /> </sys_created_date> ";
			String  sys_created_by = "<sys_created_by>WSO2</sys_created_by>";
			
			ArrayList<String> idpFileds = odhforms.getValue();
			for (int j = 0; j < idpFileds.size(); j++) {
				String idpFiled = idpFileds.get(j);
				if (idpFiled.contains("sys_last_modified")) {
					continue;
				}
				idpFiled = idpFiled.trim();
				String type = idpTypes.get(tableName).get(j);
				if (type == null) {
					type = "";
				} else {
					type = type.trim().toLowerCase();
				}
				System.out.println(type);
				
				if(idpFiled.trim().equalsIgnoreCase("sys_created_date")){
					xmlFragment +=sys_created_date;continue;
				}
				
				if(idpFiled.trim().equalsIgnoreCase("sys_created_by")){
					xmlFragment +=sys_created_by;continue;
				}
				
				
				if (type.contains("timestamp")) {
					xmlFragment += maynullTimeStamp.replace("{id}",
							idpFiled.toLowerCase());
				}else if(type.contains("int")){
					xmlFragment += maynullInt.replace("{id}",
							idpFiled.toLowerCase());
				}else if (true) {
					xmlFragment += cannotnull.replace("{id}",
							idpFiled.toLowerCase());
				}
				
				
			}

			System.out.println(xmlFragment);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see SFDC2IDP.BASE.COMMON.IMappingSolver#getSFDCFields()
	 */
	@Override
	public HashMap<String, ArrayList<String>> getSFDCFields() {
		return SFDCFields;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see SFDC2IDP.BASE.COMMON.IMappingSolver#setSFDCFields(java.util.HashMap)
	 */
	@Override
	public void setSFDCFields(HashMap<String, ArrayList<String>> sFDCFields) {
		SFDCFields = sFDCFields;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see SFDC2IDP.BASE.COMMON.IMappingSolver#getIDPFields()
	 */
	@Override
	public HashMap<String, ArrayList<String>> getIDPFields() {
		return IDPFields;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see SFDC2IDP.BASE.COMMON.IMappingSolver#setIDPFields(java.util.HashMap)
	 */
	@Override
	public void setIDPFields(HashMap<String, ArrayList<String>> iDPFields) {
		IDPFields = iDPFields;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see SFDC2IDP.BASE.COMMON.IMappingSolver#getIDPFieldTypes()
	 */
	@Override
	public HashMap<String, ArrayList<String>> getIDPFieldTypes() {
		return IDPFieldTypes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * SFDC2IDP.BASE.COMMON.IMappingSolver#setIDPFieldTypes(java.util.HashMap)
	 */
	@Override
	public void setIDPFieldTypes(
			HashMap<String, ArrayList<String>> iDPFieldTypes) {
		IDPFieldTypes = iDPFieldTypes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * SFDC2IDP.BASE.COMMON.IMappingSolver#setTableNames(java.util.ArrayList)
	 */
	@Override
	public void setTableNames(ArrayList<String> tableNames) {
		this.tableNames = tableNames;
	}

	private ArrayList<String> tableNames = new ArrayList<String>();
	private HashMap<String, ArrayList<String>> SFDCFields = new HashMap<String, ArrayList<String>>();
	private HashMap<String, ArrayList<String>> IDPFields = new HashMap<String, ArrayList<String>>();
	private HashMap<String, ArrayList<String>> IDPFieldTypes = new HashMap<String, ArrayList<String>>();
	private HashMap<String, String> tableName2ObjectName = new HashMap<String, String>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see SFDC2IDP.BASE.COMMON.IMappingSolver#getTableName2ObjectName()
	 */
	@Override
	public HashMap<String, String> getTableName2ObjectName() {
		return tableName2ObjectName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * SFDC2IDP.BASE.COMMON.IMappingSolver#setTableName2ObjectName(java.util
	 * .HashMap)
	 */
	@Override
	public void setTableName2ObjectName(
			HashMap<String, String> tableName2ObjectName) {
		this.tableName2ObjectName = tableName2ObjectName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see SFDC2IDP.BASE.COMMON.IMappingSolver#getTableNames()
	 */
	@Override
	public ArrayList<String> getTableNames() {
		return tableNames;
	}

	public MDS2ODHDSS_Fragment() throws Exception {
	      String createSqls = Helper.getFileContent(MDS2ODH.MAIN.CONSTANTS.sqlStructFilePath);
		String[] arrayCreateSqls = createSqls.split("CREATE TABLE");
		// System.out.println(createSqls);
		for (String creatSqlString : arrayCreateSqls) {
			if (creatSqlString.length() > 10) {
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
		if (tableName.startsWith(suffix)) {
			tableName = tableName.substring(suffix.length()).replace("_Temp",
					"");
		}
		System.out.println("table name = " + tableName);
		tableNames.add(tableName);

		// 获取字段

		String fieldPart = creatSqlString.substring(
				creatSqlString.indexOf("(") + 1, creatSqlString.length())
				.trim();

		String fields[] = fieldPart.split("NULL\\,");
		// System.out.println("fields count = " + fields.length);
		// System.out.println("fields = " + Arrays.toString(fields));
		for (String fieldAndType : fields) {
			String IDPField = "";
			String IDPFieldType = "";

			try {
				String[] fieldAndTypeArray = fieldAndType.trim()
						.replaceAll("\\s+", " ").split(" ");
				IDPField = fieldAndTypeArray[0].trim();
				IDPFieldType = fieldAndTypeArray[1].trim();
				;
				// System.err.println(Arrays.toString(fieldAndType.trim().split("\\s")));
			} catch (Exception e) {
				System.err.println("field prase fail:" + IDPField + ","
						+ IDPFieldType);
			}

			initMap(IDPFields, tableName);
			initMap(IDPFieldTypes, tableName);

			IDPFields.get(tableName).add(IDPField.trim());
			IDPFieldTypes.get(tableName).add(IDPFieldType);
		}
	}

	private void initMap(HashMap<String, ArrayList<String>> map, String key) {
		if (!map.containsKey(key)) {
			map.put(key, new ArrayList<String>());
		}
	}

}