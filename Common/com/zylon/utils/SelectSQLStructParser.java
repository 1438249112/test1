package com.zylon.utils;

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
			 response += "{\"result\":{\"exchangeRates\":[{ ";
			for (String field : e.getValue()) {
				if(field.startsWith("m") && field.length()<4){
					response +="\""+field.toUpperCase()+"_Rate"+"\":"+"\"$"+field+"(type:double)\",";
				}else{
					response +="\"CurrencyName\":"+"\"$"+field+"\",";
				}
			}
			 response = Helper.deleteLastChar(response);
			 response += "}]}}";
			
			response+="\n";
		}
		return response;
	}
	
	public static void main(String[] args) throws Exception {
		HashMap<String, ArrayList<String>> t = new SelectSQLStructParser("SELECT CURRENCY_NAME ,M1 ,M2 ,M3 ,M4 ,M5 ,M6 ,M7 ,M8 ,M9 ,M10 ,M11 ,M12  FROM PUBLIC.EXP_CFE_EXCHANGE_RATE LIMIT 5000")
				.getTableFields();
		System.out.println(t.size());
		System.out.println(new SelectSQLStructParser("select currency_name ,m1 ,m2 ,m3 ,m4 ,m5 ,m6 ,m7 ,m8 ,m9 ,m10 ,m11 ,m12  from public.exp_cfe_exchange_rate limit 5000")
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