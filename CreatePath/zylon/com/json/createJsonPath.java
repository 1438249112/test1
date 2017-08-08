package zylon.com.json;

import java.util.ArrayList;
import java.util.Map.Entry;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import SFDC2IDP.BASE.COMMON.Helper;

public class createJsonPath {
private static String path = "E:/lenovo-work/work/empr_part/parts.json";
public static void main(String[] args) throws Exception {
 String content	= Helper.getClipOrFileContent(path);
// System.out.println(content);

 
 getJsonPath(content,"$",true);
 getJsonPath(content,"$",false);
}

private static ArrayList<String>  getJsonPath(String content,String jsonPath,boolean ispath){
	try {
		 JSONObject object = JSON.parseObject(content);
			if(object instanceof JSONObject){
				 for (Entry<String, Object> entry : ((JSONObject)object).entrySet()) {
//					  System.out.println(entry.getKey());
					  getJsonPath(entry.getValue().toString(), jsonPath+"."+entry.getKey().trim(),ispath);
				}
			}
	} catch (Exception e) {
		try {
			 JSONArray object = JSON.parseArray(content);
			 jsonPath =jsonPath.trim()+"[0]";
			 getJsonPath(object.getString(0), jsonPath,ispath);
		} catch (Exception e1) {
			if(ispath){
				System.out.println(jsonPath);

			}else{
				System.out.println(jsonPath.substring(jsonPath.lastIndexOf(".")+1,jsonPath.length()).trim());
			}
		}
	}
	
	
	return null;  
  }
}
