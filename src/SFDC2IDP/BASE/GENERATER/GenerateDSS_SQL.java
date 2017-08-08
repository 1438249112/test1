package SFDC2IDP.BASE.GENERATER;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import SFDC2IDP.BASE.COMMON.CONSTANTS;
import SFDC2IDP.BASE.COMMON.CreateSQLFileSolver;
import SFDC2IDP.BASE.COMMON.Helper;
import SFDC2IDP.BASE.INTERFACE.AbstractResusltGenerater;
import SFDC2IDP.BASE.INTERFACE.IMappingHandler;
import SFDC2IDP.BASEIDPSQL.MAPPINGHANDLER.Handle_Mapping_BaseSQL;
import SFDC2IDP.BASESFDCEXCEL.MAPPINGHANDLER.Handle_Mapping_BaseExcel;

public class GenerateDSS_SQL  extends AbstractResusltGenerater  {
public GenerateDSS_SQL(IMappingHandler handle_SFDC2IDP_Mapping) {
		super(handle_SFDC2IDP_Mapping);
		// TODO Auto-generated constructor stub
	}

public void execute() throws Exception{

			
			for (Entry<String, String> tableNameAndSql : handle_SFDC2IDP_Mapping.getSqlMap().entrySet()) {
				String SalesforceTableName = tableNameAndSql.getKey().replace("SFDC_", "");
				Helper.writerFile(CONSTANTS.LOCAL_Results_BasePath+"/SFDC2TELE_"+SalesforceTableName+"/DSS_SQL.xml", tableNameAndSql.getValue());
			}
}	
}