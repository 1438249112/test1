package SFDC2IDP.BASE.COMMON;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class CONSTANTS {
public static final String skipDealTableNames ="SFDC_Account1,SFDC_CustomerHasIT1";
public static final String dealTableNames ="";
public static HashMap<String,String> colors = new HashMap<String,String> ();
//public static final String dealTableNames ="SFDC_CustomerHasIT,SFDC_Account,SFDC_Contact,SFDC_LinkmanFamliylink";

public static SimpleDateFormat dateformate = new SimpleDateFormat("yyyyMMdd");
public static final String SFDC2IDP_Mapping_FilePath ="E:/lenovo-work/work/Salesforce2IDP/sunxiao/SFDC2TELE_PROXY_GEN/resouces/SFDC IDP Mapping.xlsx";
public static final String IDP_CreateTable_FilePath ="E:/lenovo-work/work/Salesforce2IDP/sunxiao/SFDC2TELE_PROXY_GEN/resouces/database.sql";
public static final String WSO2_Proxy_Template_FilePath ="E:/lenovo-work/work/Salesforce2IDP/sunxiao/SFDC2TELE_PROXY_GEN/resouces/SFDC2TELE_Account.xml";
public static final String WSO2_Mapping_In_Template_FilePath ="E:/lenovo-work/work/Salesforce2IDP/sunxiao/SFDC2TELE_PROXY_GEN/resouces/datamapper_inputSchema.json";
public static final String WSO2_Mapping_DCM_Template_FilePath ="E:/lenovo-work/work/Salesforce2IDP/sunxiao/SFDC2TELE_PROXY_GEN/resouces/datamapper.dmc";

public static final String WSO2_Mapping_Out_Template_FilePath ="E:/lenovo-work/work/Salesforce2IDP/sunxiao/SFDC2TELE_PROXY_GEN/resouces/datamapper_outputSchema.json";

public static  String LOCAL_Results_BasePath ="";
  static {   
	CONSTANTS.LOCAL_Results_BasePath = "E:/lenovo-work/work/Salesforce2IDP/sunxiao/SFDC2TELE_PROXY_GEN/result_files_"+dateformate.format(new Date());
	}

public static final String WSO2_XSLT_Template_FilePath ="E:/lenovo-work/work/Salesforce2IDP/sunxiao/SFDC2TELE_PROXY_GEN/resouces/SFDC2TELE_Account.xslt";
public static final String WSO2_SFDCObjectStruct_URL ="https://soa-test.lenovo.com/esb2/services/POC_SFDC2IDP_GetStructure/";

//params in DCM file
public static final String mapping_var_date = "date";
//datetime_formate;decimal_fromate;int_fromate;
public static final String mapping_function_datetime_formate = "datetime_formate";
public static final String mapping_function_decimal_fromate = "decimal_fromate";
public static final String mapping_function_int_fromate = "int_fromate";
public static final String mapping_batch_flag = "batchno";

public static final String outputObjectSuffix = "outputresult.insert2sfdc_account_temp_batch_req.insert2sfdc_account_temp[count_i_records].";	
public static final String inputObjectSuffix = "inputjsonObject.records[i_records].";
//params in out schema
public static final String out_schema_fraction_template = "\"{keyWord}\": { \"id\": \"http://wso2jsonschema.org/insert2sfdc_account_temp_batch_req/insert2sfdc_account_temp/0/{keyWord}\", \"type\": \"string\" },";
//params in in schema
public static final String in_schema_fraction_template = "\"{keyWord}\" : { \"id\" : \"http://wso2jsonschema.org/records/{keyWord}\", \"type\" : \"string\" },";
}