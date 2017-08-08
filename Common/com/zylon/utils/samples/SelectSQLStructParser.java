package com.zylon.utils.samples;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import com.zylon.sap.main.Generator;

import SFDC2IDP.BASE.COMMON.Helper;
import SFDC2IDP.BASE.INTERFACE.IMappingSolver;

public class SelectSQLStructParser {
	
	public String createSelectSql() {
		String select = "";
		for (Entry<String, ArrayList<String>> e : getTableFields().entrySet()) {
			 select += "select ";
			for (String field : e.getValue()) {
				select +=field+",";
			}
			select = Helper.deleteLastChar(select);
			select+=" from "+e.getKey()+"";
		}
		return select;
	}
	public String dssJsonResponse() {
		String response = "";
		for (Entry<String, ArrayList<String>> e : getTableFields().entrySet()) {
			 response += "{\"data\":{\"customers\":[{ ";
			for (String field : e.getValue()) {
				
					response +="\""+field+"\":"+"\"$"+field+"\",";
				
			}
			 response = Helper.deleteLastChar(response);
			 response += "}]}}";
			
			response+="\n";
		}
		return response;
	}
	
	public static void main(String[] args) throws Exception {
	
		System.out.println(new SelectSQLStructParser("select company_duns_number,company_name,address,street,town,county,postcode,country,telephone_number,national_id,national_id_type,head_quarters_duns_number,domestic_ultimate_duns_number,domestic_ultimate_name,global_ultimate_duns_number,us_1987_primary_sic_code,opts_out_of_direct_mktg from public.dnb_end_customer_10_tmp limit 5")
				.dssJsonResponse());
	}

	public HashMap<String, ArrayList<String>> getTableFields() {
		return tableFields;
	}

	public void setTableFields(HashMap<String, ArrayList<String>> tableFields) {
		this.tableFields = tableFields;
	}

	private HashMap<String, ArrayList<String>> tableFields = new HashMap<String, ArrayList<String>>();

	public SelectSQLStructParser(String content) {
		this(content, "");
	}
	public SelectSQLStructParser(File file) {
		this(file, "");
	}
	public SelectSQLStructParser(File file, String sqlSplit) {
		File rootPathFile = file;
		if(rootPathFile.isDirectory()){
			for (File f : rootPathFile.listFiles()) {
				if(f.isDirectory()){
					continue;
				}
				parseSQL(Helper.getFileContent(f).trim().toLowerCase());
			}
		}else{
			parseSQL(Helper.getFileContent(rootPathFile).trim().toLowerCase());
		}
		
	}
	public SelectSQLStructParser(String content, String sqlSplit) {
			parseSQL(content.trim());
	}

	private void parseSQL(String selectSqlString) {

		String[] fieldsAndTableNames = selectSqlString.split("(?i)\\s*select\\s+");
		for (String fieldsAndTableName : fieldsAndTableNames) {
			if (fieldsAndTableName.length() > 10) {
				try {
					String[] fieldsAndTableNameArray = fieldsAndTableName
							.trim().split("(?i)\\s+from\\s+");
					String fields = fieldsAndTableNameArray[0].trim();
					String TableName = fieldsAndTableNameArray[1].trim().split(
							"\\s")[0];
					ArrayList<String> fieldsArray = new ArrayList<String>();
//					System.out.println(TableName);
					tableFields.put(TableName, fieldsArray);
					for (String field : fields.split(",")) {
						fieldsArray.add(field.trim());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

}