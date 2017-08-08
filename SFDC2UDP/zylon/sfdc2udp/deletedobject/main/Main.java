package zylon.sfdc2udp.deletedobject.main;

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
	/**
	 * cpq_ms_ap_approvalrequest_cdl
cpq_ms_ap_deletedapprovalrequest_cdl
cpq_ms_ap_deletedlineitem_cdl
cpq_ms_ap_deletedproductconfigration_cdl
cpq_ms_ap_deletedproposallineitem_cdl
cpq_ms_ap_deletedquotation_cdl
cpq_ms_ap_lineitem_cdl
cpq_ms_ap_productconfiguration_cdl
cpq_ms_ap_proposallineitem_cdl
cpq_ms_ap_quotation_cdl

	 * @param args
	 */
	public static void main(String[] args) {
		String infos = "na_Apttus_Approval__Approval_Request__c|cpq_ms_na_deletedapprovalrequest_idl,"
				+ "na_Apttus_Config2__Lineitem__c|cpq_ms_na_deletedlineitem_idl,"
				+ "na_Apttus_Config2__Productconfiguration__c|cpq_ms_na_deletedproductconfiguration_idl, "
				+ "na_Pricing_Contract__c|cpq_ms_na_deletedpricingcontract_idl, "
				+ "na_Apttus_Proposal__Proposal__c|cpq_ms_na_deletedquotation_idl,"
				+ "na_Apttus_Proposal__Proposal_Line_Item__c|cpq_ms_na_deletedproposallineitem_idl,"
				+ "ap_Apttus_Proposal__Proposal__c|cpq_ms_ap_deletedquotation_idl,"
				+ "ap_Apttus_Proposal__Proposal_Line_Item__c|cpq_ms_ap_deletedproposallineitem_idl,"
				+ "ap_Apttus_Config2__LineItem__c|cpq_ms_ap_deletedlineitem_idl ,"
				+ "ap_Apttus_Approval__Approval_Request__c|cpq_ms_ap_deletedapprovalrequest_idl,"
				+ "ap_Apttus_Config2__ProductConfiguration__c|cpq_ms_ap_deletedproductconfiguration_idl";
		 String sfdc_user_name ="";
		for (String string : infos.split(",")) {
			String path =  string.split("\\|", 2)[1];
			String module =  string.split("\\|", 2)[0];
			String[] rAo = module.split("_", 2);
			String region = rAo[0].trim();
			String object = rAo[1].trim();
//			System.out.println("SFDC2UDP_DELETED_OBJECTS_SYNC_"+region.toUpperCase()+"_"+rAo[1].trim());
//			System.out.println("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"> <soapenv:Header/> <soapenv:Body/> </soapenv:Envelope>");
          
			if(region.equalsIgnoreCase("na")){
        	    sfdc_user_name = "cloud_integration@lenovo.com.na.full";
           }else{
        	    sfdc_user_name = "b2b.integration@lenovo.com.full";
           }
			System.out.println("http://elb.wso2.lenovo.com:8080/esb/services/SFDC2UDP_DELETED_OBJECTS_SYNC?region="+region.toLowerCase()+"&object="+object.toLowerCase());
			 StringBuffer stringBuff = new StringBuffer();
			  
			 stringBuff.append("{")
			  .append("\"region\":\"").append(region.toUpperCase()).append("\",").append("\n")
			   .append("\"object\":\"").append(object).append("\",").append("\n")
			    .append("\"path\":\"").append(path).append("\",").append("\n")
			     .append("\"fileName\":\"").append("Deleted"+object+"_"+region.toUpperCase()).append("\",").append("\n")
			        .append("\"sfdc_user_name\":\"").append(sfdc_user_name).append("\",").append("\n")
			         .append("\"sfdc_user_env\":\"").append("test").append("\"\n")
			  .append("}");
			  
			String variables = "SFDC2UDP_DELETED_OBJECTS_SYNC/variables/";
				WSO2.write(variables+module.toLowerCase().trim()+"/query.global.latestdate.txt", "2017-07-19T23:56:00.000Z");
				WSO2.write(variables+module.toLowerCase().trim()+"/query.global.info.txt",stringBuff.toString());

			try{
				Thread.sleep(3000);
				String[] fileds = getFields(
						WSO2.triggerGET("SFDC2UDP_DELETED_OBJECTS_SYNC?region=" + region.toLowerCase() + "&object=" + object.toLowerCase()));

			} catch (Exception e) {
				e.printStackTrace();
			}
			/**
			 * 
			 * 
{
    
            "region": "AP",
            "object": "Account",
	    "fileName": "DeletedAccount_AP",
	    "path":"sfdc_ms_ap_deletedaccount_idl",
		"sfdc_user_name","sfdc_user_name",
		"sfdc_user_env","sfdc_user_env"
       
}

			 */

		}
	

	}

	private static String[] getFields(String result) {
		try {
			System.err.println("result : " + result.split("payload_size")[1]);

		} catch (Exception e) {
			System.err.println("result : " + result);

		}
		return null;

	}

}