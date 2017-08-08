package SFDC2IDP.MAIN;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.ContentEncodingHttpClient;

import SFDC2IDP.BASE.COMMON.Helper;
import SFDC2IDP.BASE.INTERFACE.AbstractResusltGenerater;
import SFDC2IDP.BASE.INTERFACE.IMappingHandler;

public class TestProxy  extends AbstractResusltGenerater {
public TestProxy(IMappingHandler handle_SFDC2IDP_Mapping) {
		super(handle_SFDC2IDP_Mapping);
		// TODO Auto-generated constructor stub
	}

public TestProxy() {

}

public void execute() throws Exception{
	HashMap<String,Boolean> state = new HashMap<String,Boolean>();
	HashMap<String,String> result =new  HashMap<String,String>();
	System.out.println("//////////////////////////////////TestProxy////////////////////////////////////");
	System.out.println("start test interfaces .......");
for (int i = 0; i <200; i++) {
         String testNo = "Test"+i;
		try {
			
			String path = "https://soa-test.lenovo.com/esb2/services/SFDC2I2_SALES_FORECAST_DETAIL1.0/"+testNo;
			ContentEncodingHttpClient httpClient = new ContentEncodingHttpClient();
			HttpResponse response = httpClient.execute(new HttpGet(path));
				if(i==0){
					System.out.println("Test  "+(i+1) + ": " + path);
				}else{
					System.out.println("Try Test  "+1 + ": " + path);
				}
				BufferedReader br = new BufferedReader(new InputStreamReader(
						response.getEntity().getContent()));
				String line = "";
				String resultString= "";
				while ((line = br.readLine()) != null) {
					if(line.contains("SUCCESSFUL。。。。。。。")){
						state.put(testNo, true);
//						System.out
//						.println("SFDC2TELE_"+SalesforceTableName +": SUCCESSFUL");
					}else{
						state.put(testNo, state.get(testNo)==null?false:state.get(testNo)||false);
						resultString+=line;
						
					}
					
				}
				System.out.println( "SFDC2I2_SALES_FORECAST_DETAIL1.0-"+testNo +" reuslt : "+resultString);
			
		} catch (IOException e) {
			e.printStackTrace();
		}

			
			
			}
//System.out.println(" test results : ");
//
//for (Entry<String, String> entry : result.entrySet()) {
//	try {
//		if(!state.get(entry.getKey())){
//			System.out.println(new String (entry.getValue().getBytes(),"UTF8"));
//		}
//
//	} catch (Exception e) {
//		e.printStackTrace();
//		System.out.println(entry.getKey());
//	}
////	}
		
System.out.println("end test interface");
}

public static void main(String[] args) throws Exception {
          new TestProxy().execute();
}
	
}