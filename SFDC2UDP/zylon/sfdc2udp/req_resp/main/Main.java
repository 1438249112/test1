package zylon.sfdc2udp.req_resp.main;

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
	private static	Excel excel	= new  Excel("E:/lenovo-work/work/SFDC2UDP_DCE_Ruquest_Response/DCE Request Response from SFDC (LBP) to UDP_20170519.xlsx");
  public static void main(String[] args) {
		
			dealWith(excel);	
}
  private static void dealWith(Excel excel) {
		ArrayList<Cell> cells = excel.getColsByBlurFirstColName("idl.dce_ms_request_response_lbp_idl");
		ArrayList<Cell> sfdccells = excel.getColsByBlurFirstColName("SFDC Object Name:");
	  StringBuffer stringBuff = new StringBuffer();
	  for (int i = 1; i < cells.size(); i++) {
		  String filedName ="";
		  String sfdcfiledName = "";
		  String valuesource = "";
		  try {
			  filedName =excel.getValue(cells.get(i)).trim().toLowerCase();
			   sfdcfiledName =excel.getValue(sfdccells.get(i)).trim();
		  
		
			  if(sfdcfiledName==null||sfdcfiledName.trim().equalsIgnoreCase("")){
				  valuesource = "$"+filedName;
			  }else{
				  valuesource = "sf:"+sfdcfiledName.replace(".", "/sf:");
			  }
			  
			  stringBuff
			  .append("<").append(filedName).append(">")
			  .append("<xsl:value-of  select=\"").append(valuesource).append("\" />")
			  .append("</").append(filedName).append(">");
	   		
		} catch (Exception e) {
			System.out.println(filedName+":"+sfdcfiledName+":"+valuesource);
		}
		
   	   }
	  System.out.println(stringBuff);
}
}