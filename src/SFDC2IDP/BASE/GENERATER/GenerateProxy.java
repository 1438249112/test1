package SFDC2IDP.BASE.GENERATER;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.UUID;
import java.util.Map.Entry;

import SFDC2IDP.BASE.COMMON.CONSTANTS;
import SFDC2IDP.BASE.COMMON.Helper;
import SFDC2IDP.BASE.INTERFACE.AbstractResusltGenerater;
import SFDC2IDP.BASE.INTERFACE.IMappingHandler;
import SFDC2IDP.BASESFDCEXCEL.MAPPINGHANDLER.Handle_Mapping_BaseExcel;

public class GenerateProxy   extends AbstractResusltGenerater {
public GenerateProxy(IMappingHandler handle_SFDC2IDP_Mapping) {
		super(handle_SFDC2IDP_Mapping);
		// TODO Auto-generated constructor stub
	}
public void execute() throws Exception{
	//1.获取模板文件
    String templateString = Helper.getFileContent(CONSTANTS.WSO2_Proxy_Template_FilePath);
      //2.dmc文件
			for (Entry<String, String> tableNameAndSql : handle_SFDC2IDP_Mapping.getSoqlMap().entrySet()) {
				String templateStringCopy = templateString;
				String SalesforceTableName = handle_SFDC2IDP_Mapping.getTableName2ObjectName().get( tableNameAndSql.getKey()).replace("SFDC_", "");
				templateStringCopy = templateStringCopy.replace("Account", SalesforceTableName);
				templateStringCopy = templateStringCopy.replace("account_temp", SalesforceTableName.toLowerCase().trim()+"_temp");

			    String basicSoql = " <property name=\"soql.var.basic\" value=\""+tableNameAndSql.getValue()+"\"/>"	;
//				System.out.println(tableNameAndSql.getValue());
			    String[] newXmlStrings = templateStringCopy.split("soql\\.var\\.basic",2);
			    
			    try {
			        String newXmlString1 = newXmlStrings[0].substring(0,newXmlStrings[0].lastIndexOf("<"));
				    String newXmlString2 = newXmlStrings[1].substring(newXmlStrings[1].indexOf(">")+1,newXmlStrings[1].length());
				    String initPath = "/_system/governance/repository/services/SFDC2TELE_"+SalesforceTableName+"/variables/query.global.latestdate\n";
				    String proxyName = "SFDC2TELE_"+SalesforceTableName+"";

				    Helper.writerFile(CONSTANTS.LOCAL_Results_BasePath+"/files/"+UUID.randomUUID()+".proxy.init.time",initPath);
					Helper.writerFile(CONSTANTS.LOCAL_Results_BasePath+"/SFDC2TELE_"+SalesforceTableName+"/proxy.xml", proxyName+newXmlString1+basicSoql+newXmlString2);
				    Helper.writerFile(CONSTANTS.LOCAL_Results_BasePath+"/proxies/SFDC2TELE_"+SalesforceTableName+".xml", proxyName+newXmlString1+basicSoql+newXmlString2);

				} catch (Exception e) {
					e.printStackTrace();
				}
			
	
			}
		

}
}