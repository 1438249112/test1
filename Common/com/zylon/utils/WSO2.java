package com.zylon.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.ContentEncodingHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.nio.entity.NStringEntity;

import SFDC2IDP.BASE.COMMON.CONSTANTS;
import SFDC2IDP.BASE.COMMON.Helper;

public class WSO2 {
	public static void main(String[] args) {
		WSO2.write("3/1.jsp", "<a>test</a>");
//		WSO2.write("3/1.html", "<a>test</a>");
//		WSO2.write("3/1.txt", "<a>test</a>");
//		WSO2.write("3/1.xml", "<a>test2</a>");
	}
	
	public static String triggerGET(
			String uri) {
		return triggerGET(uri,3);
	}
	public static String triggerGET(
			String uri,int time) {
		String url ="https://soa-test.lenovo.com/esb2/services/"+uri;
		String result = "";
	
		try {
		System.out.println(url);
			ContentEncodingHttpClient httpClient = new ContentEncodingHttpClient();
			HttpResponse response = httpClient.execute(new HttpGet(url));
			System.out.println(Helper.class.getCanonicalName()
					+ "  StatusCode:"
					+ response.getStatusLine().getStatusCode());
			if(response.getStatusLine().getStatusCode()>300 && time < 1){
				return triggerGET(uri,time--);
			}
		
			if (response.getStatusLine().getStatusCode() >= 200) {
				System.out.println(Helper.class.getCanonicalName() + ":" + url);
				BufferedReader br = new BufferedReader(new InputStreamReader(
						response.getEntity().getContent()));
				String line = "";
				while ((line = br.readLine()) != null) {
				
					
					result += line;
				}
				;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;

	}
public static void write(String path,String content) {
System.out.println("WSO2Write:"+path);
		ContentEncodingHttpClient httpClient = new ContentEncodingHttpClient();
		HttpPost post = new HttpPost("https://soa-test.lenovo.com/esb2/services/POC_Upload_Proxy_Registry_Files");
System.out.println(content);
		try {
			post.addHeader( new BasicHeader("filePath",path)); 
			post.addHeader(new BasicHeader("Content-Type","text/plain"));
			org.apache.http.HttpEntity entity = new NStringEntity(content);
			post.setEntity(entity);
			HttpResponse response = httpClient.execute(post);
			if (response.getStatusLine().getStatusCode() == 200) {
				
				BufferedReader br = new BufferedReader(new InputStreamReader(
						response.getEntity().getContent()));
				String line = "";
				while ((line = br.readLine()) != null) {
//				System.out.println(line);
				}
			
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
}
/**
 * <?xml version="1.0" encoding="UTF-8"?>
<proxy xmlns="http://ws.apache.org/ns/synapse"
       name="POC_Upload_Proxy_Registry_Files"
       startOnLoad="true"
       statistics="disable"
       trace="disable"
       transports="http,https">
   <target>
      <inSequence>
         <sequence key="SQ_COMMON_REQUEST"/>
         <log level="full">
            <property name="msg" value="POC_Upload_Proxy_Registry_Files: Start .."/>
         </log>
         <property expression="$body/*" name="request.var.payload"/>
         <property expression="$trp:filePath" name="filePath"/>
         <script language="js">var filePath = mc.getProperty("filePath").toString();  
		                   var index = filePath.lastIndexOf("/");
		                   var dir = filePath.substring(0,filePath.lastIndexOf("/"));
						   var filename = filePath.substring(filePath.lastIndexOf("/"),filePath.length());
	                       mc.getConfiguration().getRegistry().newResource("gov:/repository/services/All_File_Temp/"+dir,true);
						    mc.getConfiguration().getRegistry().newResource("gov:/repository/services/All_File_Temp/"+filePath,false);
	                       mc.getConfiguration().getRegistry().updateResource(
	                       "gov:/repository/services/All_File_Temp/"+filePath,mc.getProperty("request.var.payload").toString());
						   
						   mc.setProperty("log",filePath+"::"+dir+"::"+filename+"payload="+mc.getProperty("request.var.payload").toString());</script>
         <call-template target="TP_COMMON_SUCCESS_HANDLER">
            <with-param name="successdesc" value="{$ctx:log}"/>
            <with-param name="savepayload" value="false"/>
         </call-template>
         <respond/>
      </inSequence>
      <faultSequence>
         <log level="custom">
            <property name="msg" value="faultSequence ...."/>
         </log>
         <log level="custom"/>
         <call-template target="TP_COMMON_FAULT_HANDLER">
            <with-param name="statuscode" value="202"/>
            <with-param name="errormsg" value="{concat('; ',//faultcode)}"/>
         </call-template>
         <property name="messageType" scope="axis2" value="application/json"/>
         <property name="HTTP_SC" scope="axis2" type="STRING" value="200"/>
         <respond/>
      </faultSequence>
   </target>
   <description/>
</proxy>
                                
 */
	
}


