package SFDC2IDP.MAIN;

import java.io.File;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import SFDC2IDP.BASE.COMMON.Helper;

public class TestRegistryDelay {
public static void main(String[] args) throws Exception {
	SimpleDateFormat dateformate = new SimpleDateFormat("yyyyMMddhhmmss");
	File f = new File("E:/lenovo-work/work/Salesforce2DB/≤‚ ‘/registryDelayTest/15000delayTest_"+dateformate.format(new Date())+".txt");
	System.setErr(new PrintStream(f));
	String getResult = Helper.getRespond("https://soa-test.lenovo.com/esb1/services/Registry_Delay_Test/get");;
	long start = System.currentTimeMillis();
	String setResult = Helper.getRespond("https://soa-test.lenovo.com/esb1/services/Registry_Delay_Test/set");
	int i =0;
	while(!keyWordEqual(setResult,getResult)){
		System.out.println("run times = "+i++);
		getResult  = Helper.getRespond("https://soa-test.lenovo.com/esb1/services/Registry_Delay_Test/get");
	}
	
	long end = System.currentTimeMillis();
	  System.out.println("start ="+start);
    System.out.println("end ="+end);
    System.out.println("interval ="+(end-start));
    System.err.println("interval ="+(end-start));
	java.awt.Desktop.getDesktop().open(f);

}

private static boolean keyWordEqual(String setResult, String getResult) {
	   
	    String setUUID = getUUID(setResult);
	    String getUUID = getUUID(getResult);
	    System.out.println("set uuid ="+  setUUID);
		 System.out.println("get uuid ="+getUUID);
	 if(setUUID.equals(getUUID)){
		 System.err.println("set uuid ="+  setUUID);
		 System.err.println("get uuid ="+getUUID);
		 return true;
	 }
	return false;
}

private static String getUUID(String string) {
	try {
		return string.substring(string.indexOf("<uuid>")+"<uuid>".length(), string.indexOf("</uuid>"));
	} catch (Exception e) {
//		e.printStackTrace();
	}

	return "";
}
}
