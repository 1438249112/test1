package zylon.sfdc2udp.deletedobject.main;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.poi.ss.usermodel.Cell;

import SFDC2IDP.BASE.COMMON.Helper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zylon.utils.Excel;
import com.zylon.utils.SelectSQLStruct;
import com.zylon.utils.WSO2;

import edu.emory.mathcs.backport.java.util.Arrays;

public class PraseCastIronJson {
 public static  String rootPath = "E:/lenovo-work/work/SFDC2UDP/resources/soql archiver/";
public static void main(String[] args) {
	 	File rootPathFile = new File(rootPath);
		if(rootPathFile.isDirectory()){
			for (File file : rootPathFile.listFiles()) {
				if(file.isDirectory()){
					continue;
				}
				dealWith(file);	
			}
		}else{
		
			dealWith(rootPathFile);	
		}
}
  private static void dealWith(File file) {
	  JSONArray array = JSON.parseArray(Helper.getFileContent(file));
	  for (Object obj : array) {
		  JSONObject object = JSON.parseObject(obj.toString());
		  String region = object.get("region").toString();
		  JSONArray items = JSON.parseArray(object.get("item").toString());
		  for (Object item : items) {
			  JSONObject itemObj = JSON.parseObject(item.toString());
			  String SQL = (String) itemObj.get("SQL");
			  String objectName = (String) itemObj.get("object");
			Helper.writerFile(rootPath+"/sql/"+region.trim().toLowerCase()+"_"+objectName.trim().toLowerCase()+".sql", SQL);
		}
		 
	}
	 
	
}

}