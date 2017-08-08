package SFDC2IDP.BASE.SegmentMaker;

import java.util.HashMap;

import edu.emory.mathcs.backport.java.util.Arrays;
import SFDC2IDP.BASE.COMMON.CONSTANTS;
import SFDC2IDP.BASE.COMMON.DSSTypeGetter;
import SFDC2IDP.BASE.GENERATER.ISegmentMaker;
import net.sf.saxon.s9api.ItemType;

/**
 * result sample:
 * <data enableBatchRequests="true" name="IDP_MBG_SERVICE" transports="http https">
   <config enableOData="false" id="ODH_MBG_SERVICE">
      <property name="carbon_datasource_name">IDP_STAGE_DB</property>
   </config>
   <query id="insert2sfdc_call_list_temp" useConfig="ODH_MBG_SERVICE">
      <sql>insert into sfdc_call_list_temp (id,inputtime,name,createdbyid,createddate,lastmodifiedbyid,lastmodifieddate,sessionid__c,recordfilename__c,state__c,isconnection__c,onsitenumber__c,telnumber__c,customerid__c,cdbid__c,customername__c,contactid__c,begintime__c,endtime__c,calltime__c,callduration__c,iscallplan__c,isavailibal__c,bindingtype__c,hotline__c,channel_state__c,file_exist__c,file_size__c,ors_ip__c,recordrul__c,recordfile_generatetme__c,holdtime__c,holdendtime__c,holdperiod__c,pickupperiod__c,colinewaitperiod__c,detailtime__c,groupid__c,intime__c,peer_session_id__c,ivrsatisfyscore__c,isritcode__c,linkmanid__c) values (:id,:inputtime,:name,:createdbyid,:createddate,:lastmodifiedbyid,:lastmodifieddate,:sessionid__c,:recordfilename__c,:state__c,:isconnection__c,:onsitenumber__c,:telnumber__c,:customerid__c,:cdbid__c,:customername__c,:contactid__c,:begintime__c,:endtime__c,:calltime__c,:callduration__c,:iscallplan__c,:isavailibal__c,:bindingtype__c,:hotline__c,:channel_state__c,:file_exist__c,:file_size__c,:ors_ip__c,:recordrul__c,:recordfile_generatetme__c,:holdtime__c,:holdendtime__c,:holdperiod__c,:pickupperiod__c,:colinewaitperiod__c,:detailtime__c,:groupid__c,:intime__c,:peer_session_id__c,:ivrsatisfyscore__c,:isritcode__c,:linkmanid__c)</sql>
      <param name="id" sqlType="STRING"/>
   </query>
 
   <operation name="insert2sfdc_call_list_temp" returnRequestStatus="true">
      <call-query href="insert2sfdc_call_list_temp">
         <with-param name="id" query-param="id"/>
      </call-query>
   </operation>
    </data>
 * @author litsoft
 *
 */
public class DssSegmentMaker implements ISegmentMaker {
private String result = "";
private HashMap<String/*methodName*/,String> results = new HashMap<String,String> ();
private String queryTemplate = "<query id=\"insert2sfdc_call_list_temp\" useConfig=\"ODH_MBG_SERVICE\">${queryContent}</query>";
private String operAndCallTemplate = "<operation name=\"insert2sfdc_call_list_temp\" returnRequestStatus=\"true\"> <call-query href=\"insert2sfdc_call_list_temp\"> ${operAndCallContent}</call-query></operation>";
private String queryContentTemplate0 = " <sql>{sqlContent}</sql>";
private String queryContentTemplate1 = "  <param  defaultValue=\"#{NULL}\" name=\"id\" sqlType=\"STRING\"/>";

private String operAndCallContentTemplate = "<with-param name=\"id\" query-param=\"id\"/>";
private String tempResult = "";
private String tempQueryContent1 = "";
private String tempOperAndCallContent = "";
private HashMap<String,String> typeMap = new  HashMap<String,String>();
/* (non-Javadoc)
 * @see SFDC2IDP.BASE.SegmentMaker.ISegmentMaker#makeSegment(java.lang.String, java.lang.String, java.lang.String, java.lang.String, boolean)
 */
@Override
public void makeSegment(String tableName, String idpFiled,
		String idpFiledType, String nullString, boolean end) {
	String dssOperationName = ("Insert2"+tableName+"_Temp").trim().toLowerCase();
	typeMap.put(idpFiledType.trim().toLowerCase(), null);
	tempQueryContent1+=queryContentTemplate1.replace("id", idpFiled.trim().toLowerCase());
	tempOperAndCallContent+=operAndCallContentTemplate.replace("id", idpFiled.trim().toLowerCase());
     if(!results.containsKey(dssOperationName)){
    	 results.put(dssOperationName, tempResult);
	tempQueryContent1+=queryContentTemplate1.replace("id", CONSTANTS.mapping_batch_flag);
//	tempQueryContent1+=queryContentTemplate1.replace("STRING", DSSTypeGetter.getTypeByString(idpFiledType));
	tempOperAndCallContent+=operAndCallContentTemplate.replace("id",CONSTANTS.mapping_batch_flag);
	 }
	if(end){
		
		
		tempResult+=queryTemplate.replace("${queryContent}", queryContentTemplate0+tempQueryContent1);
		tempResult+=operAndCallTemplate.replace("${operAndCallContent}", tempOperAndCallContent);
		tempResult = tempResult.replace("insert2sfdc_call_list_temp", dssOperationName);
		results.put(dssOperationName, tempResult);
		tempResult = "";
		tempQueryContent1 = "";
		tempOperAndCallContent = "";
	}
	
	
}
public void replaceSqlContent(String dssOperationName,String sqlContent) {
	results.put(dssOperationName.trim().toLowerCase(), results.get(dssOperationName.trim().toLowerCase()).replace("{sqlContent}", sqlContent));
}
/* (non-Javadoc)
 * @see SFDC2IDP.BASE.SegmentMaker.ISegmentMaker#getResult()
 */
public HashMap<String, String> getResults() {
	return results;
}

}
