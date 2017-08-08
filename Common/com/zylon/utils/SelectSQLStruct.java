package com.zylon.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import com.zylon.sap.main.Generator;

import SFDC2IDP.BASE.COMMON.Helper;
import SFDC2IDP.BASE.INTERFACE.IMappingSolver;

public class SelectSQLStruct {
	
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
	
	public static void main(String[] args) throws Exception {
		HashMap<String, ArrayList<String>> t = new SelectSQLStruct(new File(
				"E:/lenovo-work/work/CPQ CFE PWT DCG/project/dss.xml"))
				.getTableFields();
		System.out.println(t.size());
		System.out.println( new SelectSQLStruct(new File(
				"E:/lenovo-work/work/CPQ CFE PWT DCG/project/dss.xml")).createSelectSql());
	}

	public HashMap<String, ArrayList<String>> getTableFields() {
		return tableFields;
	}

	public void setTableFields(HashMap<String, ArrayList<String>> tableFields) {
		this.tableFields = tableFields;
	}

	private HashMap<String, ArrayList<String>> tableFields = new HashMap<String, ArrayList<String>>();

	public SelectSQLStruct(String content) {
		this(content, "");
	}
	public SelectSQLStruct(File file) {
		this(file, "");
	}
	public SelectSQLStruct(File file, String sqlSplit) {
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
	public SelectSQLStruct(String content, String sqlSplit) {
			parseSQL(content.trim().toLowerCase());
	}

	private void parseSQL(String selectSqlString) {

		String[] fieldsAndTableNames = selectSqlString.split("\\s*select\\s+",2);
		for (String fieldsAndTableName : fieldsAndTableNames) {
			if (fieldsAndTableName.length() > 10) {
				try {
					String[] fieldsAndTableNameArray = fieldsAndTableName
							.trim().split("\\s+from\\s+");
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