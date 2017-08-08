package zylon.com.xml;


import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

import SFDC2IDP.BASE.COMMON.Helper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class createXPath {
private static String path = "E:/lenovo-work/work/PARTS SALES API/doc/20170421/sample/sample_850.xml";
public static void main(String[] args) throws Exception {
//	 String content	= Helper.getClipOrFileContent(path);
	 String content	= Helper.getFileContent(path);
 Document document = DocumentHelper.parseText(content); 

 Element root = document.getRootElement();


 List<Node> nodes  = document.selectNodes("//*");
 ArrayList<String> xpath = new ArrayList<String>();
for (Node node : nodes) {
	try {
		if(!node.getText().trim().equalsIgnoreCase("")){
//			System.out.println(node.getText());
//			System.out.println(node.getPath());
			addPath(xpath,node.getPath());
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
}
for (String string : xpath) {
	System.out.println(string+"\t"+string.substring(string.lastIndexOf("/")+1, string.length()));
}

}

private static void addPath(ArrayList<String> xpath, String path) {
	if(xpath.contains(path)){
		int index = xpath.indexOf(path);
		String oldPath = xpath.get(index);
//	System.out.println("od="+oldPath);
	 xpath.set(index, "array:"+oldPath);
	}else{
		xpath.add(path);
	}
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
