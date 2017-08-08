package zylon.sfdc2udp.pipeline.main;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.poi.ss.usermodel.Cell;

import SFDC2IDP.BASE.COMMON.Helper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zylon.utils.Excel;
import com.zylon.utils.SelectSQLStruct;
import com.zylon.utils.WSO2;

import edu.emory.mathcs.backport.java.util.Arrays;

public class Main {
	private static	Excel excel	= new  Excel("E:/lenovo-work/work/SFDC2UDP/resources/config.xlsx");
  public static void main(String[] args) {

//	     ArrayList<Cell>	cells  = excel.getColsByFirstColName("鐢熶骇鐜-闈瀌elete琛�","sfdc_ms_la_account_idl");
//	     for (Cell cell : cells) {
//				System.out.println(excel.getValue(cell));
//			}
//	 	
	  
	 	File rootPath = new File("E:/lenovo-work/work/SFDC2UDP/resources/soql archiver/sql/test/");
		if(rootPath.isDirectory()){
			for (File file : rootPath.listFiles()) {
				if(file.isDirectory()){
					continue;
				}
				
				dealWith(file,excel);	
			}
		}else{
		
			dealWith(rootPath,excel);	
		}
}
 private static  String fileName = "";
  private static void dealWith(File file, Excel excel) {
	SelectSQLStruct t = new SelectSQLStruct(file);
	 fileName = file.getName().replace(".sql", "");
	ArrayList<Cell> cells = excel.getColsByFirstColName(fileName);
	if(cells!=null){
    HashMap<String, String>	result	= getResult(t,cells);
//		System.out.println("field count = "+cells.size());
		String variables = "SFDC2UDP_Pipeline_Sync_Debug/variables/";
		
	//	variables/ap_account/query.global.info
		System.out.println(result.get("content"));
//		WSO2.write(variables+result.get("moduleName")+"/query.global.info.txt", result.get("content"));
//		WSO2.write(variables+result.get("moduleName")+"/query.global.latestdate.txt","2017-05-25T00:00:00.000Z");
//		WSO2.write(variables+result.get("moduleName")+"/queryLocator.txt","null");

//		try {
//			Thread.currentThread().sleep(3000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		String[] fileds = getFields(WSO2.triggerGET("SFDC2UDP_Pipeline_Sync_Debug?region="+result.get("region")+"&object="+result.get("object")));
//		if(fileds==null||fileds.length==0){
//			System.err.println(fileName+" can't get a success response.");
//           return;
//		}
////		for (int i = 0; i < fileds.length; i++) {
////			String nameInExcel = excel.getValue(cells.get(i+6));
////			String respondedNFieldName = fileds[i].contains(":")?fileds[i].substring(3):fileds[i];
////			if(!respondedNFieldName.equals("null")&&!respondedNFieldName.equalsIgnoreCase(nameInExcel)){
////				System.err.println(i+"	"+result.get("moduleName")+"	"+respondedNFieldName +"	"+nameInExcel);
////			}
////		}
//		boolean haserror = false;
//		for (int i = 6; i < cells.size(); i++) {
//			String nameInExcel = excel.getValue(cells.get(i));
//			String respondedNFieldName  = "";
//			try {
//				 respondedNFieldName = fileds[i-6].contains(":")?fileds[i-6].substring(3):fileds[i-6];
//			} catch (Exception e) {
//			}
//			 if(!nameInExcel.equals("batch_number")&&!respondedNFieldName.equals("null")&&!respondedNFieldName.equalsIgnoreCase(nameInExcel)){
//				 haserror = true;
////					System.err.println(i+"	"+result.get("moduleName")+"	"+respondedNFieldName +"	"+nameInExcel);
//			}
//		}
//		if(!haserror){
//			System.err.println(fileName+".sql has run perfectly . ");
//		}
//	}else{
//		System.err.println("can not find fileds in excel about "+fileName);
	}

}

private static String[] getFields(String result) {
	if(result.contains("<status>FAILED</status>")||result.contains("CONNECT_ERROR")){
		System.err.println(result);
		return new String[0];
	}
	try {
		return result.substring(result.indexOf(">")+1,result.lastIndexOf("<")).split("\\s+");

	} catch (Exception e) {
		System.err.println(result);
		return null;
	}
}

private static HashMap<String, String> getResult(SelectSQLStruct t, ArrayList<Cell> cells) {
	 HashMap<String, ArrayList<String>> table = t.getTableFields();
	 String sfdcTableName =  table.keySet().iterator().next();
	  ArrayList<String> sfdcTableNameFilelds = table.get(sfdcTableName);
	  ArrayList<String> newTableFields = new  ArrayList<String> ();
	  String nullPositonInNew = "";
	  String batchNoPostionInNew = "";
	  int lastPosition = -1;
	  int offset = 1;
	  int time = 1;
	  int temtimes = -1;
	  String keepLastModifiedDate = "false";
	  for (int i = 6; i < cells.size(); i++) {
		  String filedName =excel.getValue(cells.get(i)).trim().toLowerCase();
		  if(filedName.equalsIgnoreCase("LastModifiedDate")){
			  newTableFields.add(filedName);
			  keepLastModifiedDate = "true";
			  continue;
		  }
		  if(filedName.contains("createdby")){
			System.out.println(filedName);  
		  }
		  int position = (i-offset);
		  int newArrayPosition = newTableFields.size()+1;
   	   if(sfdcTableNameFilelds.contains(filedName)){
   		newTableFields.add(filedName);
   	   }else  if(sfdcTableNameFilelds.contains(filedName.replace("__c", "__r")+".name")){
   		newTableFields.add(filedName.replace("__c", "__r")+".name");
   	   }else{
   		   if(filedName.equalsIgnoreCase("batch_number")){
   			batchNoPostionInNew = newArrayPosition+"";
   			continue;
   		   }
   			   if(position-lastPosition==1){
   				++time;
   			   }else{
   				if(lastPosition==-1){
   					nullPositonInNew+= "|"+newArrayPosition;
   				}else{
   					nullPositonInNew+="-"+time+"|"+newArrayPosition;
   				}
   				temtimes = time;
   				time = 1;
   			   }
   				
//   		   }
   		  lastPosition = position;
   		   System.err.println(filedName+" can not find in sfdc "+sfdcTableName+"("+fileName+".sql) ;");
   	   }
	}
	  if(temtimes>0){
		  nullPositonInNew+="-"+time+"|";
	  }
	  if(batchNoPostionInNew.equals(newTableFields.size()+1+"")){
		  batchNoPostionInNew = "-1";
	  }
	  if(nullPositonInNew.contains("|"+(newTableFields.size()+1)+"-")){
		  nullPositonInNew = nullPositonInNew.replace("|"+(newTableFields.size()+1)+"-", "|-1-");
	  }
	  if(keepLastModifiedDate.equalsIgnoreCase("false")){
		  newTableFields.add("LastModifiedDate");
	  }
	 
	  table.put(sfdcTableName, newTableFields);
	  String region = excel.getValue(cells.get(1)).trim();
	  String object = excel.getValue(cells.get(2)).trim();
	  String path = excel.getValue(cells.get(3)).trim();
	  String fileName = excel.getValue(cells.get(4)).trim();
	  String batchCount = excel.getValue(cells.get(5)).trim().split("\\.")[0]+"";
//	  batchCount="1";
	  StringBuffer stringBuff = new StringBuffer();
	  
	  stringBuff.append("{")
	  .append("\"region\":\"").append(region).append("\",").append("\n")
	   .append("\"object\":\"").append(object).append("\",").append("\n")
	    .append("\"path\":\"").append(path).append("\",").append("\n")
	     .append("\"fileName\":\"").append(fileName).append("\",").append("\n")
	      .append("\"batchCount\":\"").append(batchCount).append("\",").append("\n")
	       .append("\"batch_no_position\":\"").append(batchNoPostionInNew).append("\",").append("\n")
	        .append("\"null_position\":\"").append(nullPositonInNew).append("\",").append("\n")
	         .append("\"keepLastModifiedDate\":\"").append(keepLastModifiedDate).append("\",").append("\n")
	          .append("\"sql\":\"").append(t.createSelectSql()).append("\"").append("\n")
	  .append("}");
	  HashMap<String,String> result = new   HashMap<String,String> ();
	  result.put("content", stringBuff.toString());
	  result.put("moduleName", region.toLowerCase()+"_"+object.toLowerCase());
	  result.put("region",region.toLowerCase());
	  result.put("object",object.toLowerCase());

     return result;
}

}