package SFDC2IDP.BASE.SegmentMaker;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import edu.emory.mathcs.backport.java.util.Arrays;
import SFDC2IDP.BASE.COMMON.CONSTANTS;
import SFDC2IDP.BASE.COMMON.DSSTypeGetter;
import SFDC2IDP.BASE.GENERATER.ISegmentMaker;
import net.sf.saxon.s9api.ItemType;

public class InputSechmaSegmentMaker implements ISegmentMaker {
private HashMap<String/*methodName*/,String> results = new HashMap<String,String> ();
public  String inner_schema ="\"${childTableName}\":{ \"id\":\"http://wso2jsonschema.org/records/0/${childTableName}\", \"type\":\"object\", "
		+ "\"properties\":{ \"attributes\":{ \"id\":\"http://wso2jsonschema.org/records/0/${childTableName}/attributes\", \"type\":\"object\","
		+ " \"properties\":{ \"type\":{ \"id\":\"http://wso2jsonschema.org/records/0/${childTableName}/attributes/type\", \"type\":\"string\" },"
		+ " \"url\":{ \"id\":\"http://wso2jsonschema.org/records/0/${childTableName}/attributes/url\", \"type\":\"string\" } } },"
		+ "\"Id\": { \"id\": \"http://wso2jsonschema.org/records/0/${childTableName}/Id\", \"type\": \"string\" },"
		+ " ${inner_schema_child} } },";

public  String inner_schema_child =" \"{keyWord}\":{ \"id\":\"http://wso2jsonschema.org/records/0/${childTableName}/{keyWord}\", \"type\":\"string\" },";
private HashMap<String,String> childrenMap = new  HashMap<String,String>();
private HashMap<String,String> inner_schema_childStrings = new  HashMap<String,String>();
/* (non-Javadoc)
 * @see SFDC2IDP.BASE.SegmentMaker.ISegmentMaker#makeSegment(java.lang.String, java.lang.String, java.lang.String, java.lang.String, boolean)
 */
@Override
public void makeSegment(String resultKey, String idpFiled,
		String idpFiledType, String sfdcFiled, boolean end) {
	
	 resultKey = resultKey.trim();
     if(!results.containsKey(resultKey)){
    	 results.put(resultKey, "");
	 }
     String resultPart = results.get(resultKey);
 	if(sfdcFiled.contains(".")){
		String field = sfdcFiled.substring(sfdcFiled.indexOf(".")+1);
		String childTableName = sfdcFiled.substring(0,sfdcFiled.indexOf("."));
		if(!field.trim().equalsIgnoreCase("id")){
			inner_schema_childStrings.put(resultKey+childTableName,(inner_schema_childStrings.get(resultKey+childTableName)==null?"":inner_schema_childStrings.get(resultKey+childTableName))+inner_schema_child.replace("${childTableName}", childTableName).replace("{keyWord}", field));
			childrenMap.put(resultKey+childTableName, inner_schema.replace("${childTableName}", childTableName));
		}
		
	}else{
		resultPart+= CONSTANTS.in_schema_fraction_template.replace("{keyWord}", sfdcFiled);
	}
 
 	
	if(end){
		String inner_schema_String = "";
		for (Entry<String, String> child : childrenMap.entrySet()) {
			String innerSchema_childStrings = inner_schema_childStrings.get(child.getKey()).substring(0,inner_schema_childStrings.get(child.getKey()).length()-1);
			inner_schema_String+=child.getValue().replace("${inner_schema_child}", innerSchema_childStrings);
		}
		if(!inner_schema_String.equalsIgnoreCase("")){
		
			inner_schema_String = inner_schema_String.substring(0,inner_schema_String.length()-1);

		}
		resultPart+= inner_schema_String;
		resultPart = resultPart.substring(0,resultPart.length()-1);
	     childrenMap.clear();
	      inner_schema_childStrings.clear();
	
	}
	results.put(resultKey,resultPart);
	
}
public HashMap<String, String> getResults() {
	return results;
}

}
