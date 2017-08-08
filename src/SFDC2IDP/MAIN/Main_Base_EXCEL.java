package SFDC2IDP.MAIN;

import java.io.File;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Map.Entry;
import java.util.UUID;

import SFDC2IDP.BASE.COMMON.CONSTANTS;
import SFDC2IDP.BASE.COMMON.Helper;
import SFDC2IDP.BASE.GENERATER.GenerateDSS_SQL;
import SFDC2IDP.BASE.GENERATER.GenerateMappingDMC;
import SFDC2IDP.BASE.GENERATER.GenerateMappingIn_Schema;
import SFDC2IDP.BASE.GENERATER.GenerateMappingOut_Schema;
import SFDC2IDP.BASE.GENERATER.GenerateProxy;
import SFDC2IDP.BASE.GENERATER.TestProxy;
import SFDC2IDP.BASE.INTERFACE.IMappingHandler;
import SFDC2IDP.BASESFDCEXCEL.MAPPINGHANDLER.Handle_Mapping_BaseExcel;
import SFDC2IDP.BASESFDCEXCEL.MAPPINGHANDLER.Handle_Mapping_BaseExcel_1;

public class Main_Base_EXCEL {
	public static void main(String[] args) throws Exception {
//		File f = new File(CONSTANTS.LOCAL_Results_BasePath+"/files/"+UUID.randomUUID()+".log");
//		System.setErr(new PrintStream(f));
		IMappingHandler handle = Handle_Mapping_BaseExcel_1.getInstance(false,false);
		for (Entry<String, String> entry : CONSTANTS.colors.entrySet()) {
			System.out.println(entry.getKey()+"="+entry.getValue());
		}
		new GenerateDSS_SQL(handle).execute();
		Helper.clearFiles(CONSTANTS.LOCAL_Results_BasePath+"/files/");
		new GenerateMappingDMC(handle).execute();
		new GenerateMappingIn_Schema(handle).execute();
		new GenerateMappingOut_Schema(handle).execute();
		new GenerateProxy(handle).execute();
//		new TestProxy(handle).execute();
//		java.awt.Desktop.getDesktop().open(f);
	}
}
