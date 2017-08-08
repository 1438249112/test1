package SFDC2IDP.BASE.GENERATER;

import java.io.File;
import java.util.UUID;
import java.util.Map.Entry;

import SFDC2IDP.BASE.COMMON.CONSTANTS;
import SFDC2IDP.BASE.COMMON.Helper;
import SFDC2IDP.BASE.INTERFACE.AbstractResusltGenerater;
import SFDC2IDP.BASE.INTERFACE.IMappingHandler;
import SFDC2IDP.BASEIDPSQL.MAPPINGHANDLER.Handle_Mapping_BaseSQL;
import SFDC2IDP.BASESFDCEXCEL.MAPPINGHANDLER.Handle_Mapping_BaseExcel;

public class GenerateMappingDMC  extends AbstractResusltGenerater {
public GenerateMappingDMC(IMappingHandler handle_SFDC2IDP_Mapping) {
		super(handle_SFDC2IDP_Mapping);
		// TODO Auto-generated constructor stub
	}
public void execute() throws Exception{
			//1.获取模板文件
	          File templateFile = new File(CONSTANTS.WSO2_Mapping_DCM_Template_FilePath);
	      String templateString = Helper.getFileContent(templateFile);
	      String fileName = templateFile.getName();
	        //2.dmc文件
			for (Entry<String, String> tableNameAndMappingFraction : handle_SFDC2IDP_Mapping.getMappingFraction().entrySet()) {
				String templateStringCopy = templateString;
//				System.out.println(tableNameAndMappingFraction.getValue());
				String SalesforceTableName = handle_SFDC2IDP_Mapping.getTableName2ObjectName().get( tableNameAndMappingFraction.getKey()).replace("SFDC_", "");
				templateStringCopy = templateStringCopy.replace("{content}", tableNameAndMappingFraction.getValue());
				
				templateStringCopy = templateStringCopy.replace("account_temp", SalesforceTableName.toLowerCase().trim()+"_temp");
				String path = "/_system/governance/repository/services/"+"SFDC2TELE_"+SalesforceTableName+"/modules/default/transform/"+fileName+"\n";
				Helper.writerFile(CONSTANTS.LOCAL_Results_BasePath+"/SFDC2TELE_"+SalesforceTableName+"/"+fileName,path+templateStringCopy);
				Helper.writerFile(CONSTANTS.LOCAL_Results_BasePath+"/files/"+UUID.randomUUID()+fileName,path+ templateStringCopy);
			}
		

}
	
}