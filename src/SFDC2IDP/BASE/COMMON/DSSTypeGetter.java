package SFDC2IDP.BASE.COMMON;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class DSSTypeGetter {
private static List<String>  dssTypes = null;
//[decimail, int, tinyint, date, datetime, decimal, nvarchar, varchar]
static {
String Types = "STRING,INTEGER,REAL,DOUBLE,NUMERIC,TINYINT,SMALLINT,BIGINT,DATE,TIME,TIMESTAMP,BIT,ORACLE_REF_CURSOR,BINARY,BLOB,CLOB,STRUCT,ARRAY,UUID,VARINT,QUERY_STRING,INETADDRESS";
dssTypes =  Arrays.asList(Types.split(","));
}

private static HashMap<String,String>  dssTypeNavigation = new  HashMap<String,String>();

public static  String getTypeByString(String type){
	if(type==null || type.trim()==""){
		return "STRING";
	}
	type = typeCorrect(type.trim().toLowerCase()).toLowerCase();
	if(dssTypeNavigation.containsKey(type)){
		return dssTypeNavigation.get(type);
	}
	String typeofDss = "STRING";
	for (String dssType : dssTypes) {
		if(dssType.trim().equalsIgnoreCase(type)){
			typeofDss = dssType;
			break;
		}
	}
	dssTypeNavigation.put(type, typeofDss);
	return typeofDss;
}
private static String typeCorrect(String fuzzyType) {
	if(fuzzyType.equals("decimail")){
		return "decimal";
	}
	if(fuzzyType.contains("char")){
		return "string";
	}
	if(fuzzyType.equals("int")){
		return "INTEGER";
	}
	if(fuzzyType.equals("decimal")){
		return "NUMERIC";
	}
	
	return fuzzyType;
}
} 
