package zylon.com.json;

import java.util.ArrayList;
import java.util.Map.Entry;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import SFDC2IDP.BASE.COMMON.Helper;

public class createXPath {
private static String path = "E:/lenovo-work/work/PARTS SALES API/doc/20170421/850.json";
public static void main(String[] args) throws Exception {
 String content	= Helper.getClipOrFileContent(path);
// System.out.println(content);

 
 getJsonPath(content,"/");
}

private static ArrayList<String>  getJsonPath(String content,String jsonPath){
	try {
		 JSONObject object = JSON.parseObject(content);
			if(object instanceof JSONObject){
				 for (Entry<String, Object> entry : ((JSONObject)object).entrySet()) {
//					  System.out.println(entry.getKey());
					  getJsonPath(entry.getValue().toString(), jsonPath+"/"+entry.getKey().trim());
				}
			}
	} catch (Exception e) {
		try {
			 JSONArray object = JSON.parseArray(content);
			 jsonPath =jsonPath.trim()+"[0]";
			 getJsonPath(object.getString(0), jsonPath);
		} catch (Exception e1) {
		       String filedName = jsonPath.substring(jsonPath.lastIndexOf("/")+1,jsonPath.length()).trim();
				System.out.println(filedName+"\t"+jsonPath);
			
		}
	}
	
	
	return null;  
  }
}
