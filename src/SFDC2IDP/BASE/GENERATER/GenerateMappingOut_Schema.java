package SFDC2IDP.BASE.GENERATER;

import java.io.File;
import java.util.Date;
import java.util.UUID;
import java.util.Map.Entry;

import SFDC2IDP.BASE.COMMON.CONSTANTS;
import SFDC2IDP.BASE.COMMON.Helper;
import SFDC2IDP.BASE.INTERFACE.AbstractResusltGenerater;
import SFDC2IDP.BASE.INTERFACE.IMappingHandler;
import SFDC2IDP.BASEIDPSQL.MAPPINGHANDLER.Handle_Mapping_BaseSQL;

public class GenerateMappingOut_Schema extends AbstractResusltGenerater {

public GenerateMappingOut_Schema(IMappingHandler handle_SFDC2IDP_Mapping) {
		super(handle_SFDC2IDP_Mapping);
	}

public void execute() throws Exception{
			//1.获取模板文件
	          File templateFile = new File(CONSTANTS.WSO2_Mapping_Out_Template_FilePath);
	       String templateString = Helper.getFileContent(templateFile);
	       String fileName = templateFile.getName();
	        //2.dmc文件
			for (Entry<String, String> tableNameAndMappingOutScheamFraction : super.handle_SFDC2IDP_Mapping.getMapping_out_schema_Fraction().entrySet()) {
				String templateStringCopy = templateString;
				String SalesforceTableName = super.handle_SFDC2IDP_Mapping.getTableName2ObjectName().get( tableNameAndMappingOutScheamFraction.getKey()).replace("SFDC_", "");
				templateStringCopy = templateStringCopy.replace("{content}", tableNameAndMappingOutScheamFraction.getValue());
				templateStringCopy = templateStringCopy.replace("account_temp", SalesforceTableName.toLowerCase().trim()+"_temp");
				
				
				String path = "/_system/governance/repository/services/"+"SFDC2TELE_"+SalesforceTableName+"/modules/default/transform/"+fileName+"\n";
				Helper.writerFile(CONSTANTS.LOCAL_Results_BasePath+"/SFDC2TELE_"+SalesforceTableName+"/"+fileName, path+templateStringCopy);
				Helper.writerFile(CONSTANTS.LOCAL_Results_BasePath+"/files/"+UUID.randomUUID()+fileName,path+ templateStringCopy);
			}
			
}
	
}