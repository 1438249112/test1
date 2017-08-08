package com.zylon.main;
import java.util.HashMap;
import java.util.Map.Entry;

public class Main {
private static HashMap<String,String> attrsInScope = new  HashMap<String,String>();
public static void main(String[] args) {
	init();
	String template =  "<property action=\"remove\" name=\"audit.var.tem\" scope=\"axis2\"/> "
			+ "<property expression=\"get-property('To')\" name=\"audit.var.tem\" scope=\"axis2\"/>"
			+ " <filter regex=\".+\" source=\"$ctx:audit.var.tem\"> "
			+ "<then>"
			+ " <property expression=\"concat($ctx:audit.var.sys_infos,'To:',$ctx:audit.var.tem,';')\" name=\"audit.var.sys_infos\"/>"
			+ " </then> "
			+ "<else/>"
			+ " </filter>";
	String result = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+ "<template name=\"COMMON_AUDIT_LOG_SYS_INFO\" xmlns=\"http://ws.apache.org/ns/synapse\">"
			+ "<sequence>"
			+ "<property name=\"audit.var.sys_infos\" value=\"[\"/>";
	for (Entry<String, String> entry : attrsInScope.entrySet()){
		String templateCopy =template;
		String scope = entry.getKey();
		String values = entry.getValue();
		for (String attrkey : values.trim().split(",")) {
			if(attrkey!=null&&!attrkey.equals(""))
				result+=templateCopy.replace("To",attrkey).replace("axis2", scope);	
		}
	}
	result+= "<property expression=\"concat($ctx:audit.var.sys_infos,']')\" name=\"audit.var.sys_infos\" />"
			+ "</sequence>"
			+ "</template>";
	System.out.println(result);
}

private static void init() {
	String GenericProperties = "PRESERVE_WS_ADDRESSING,RESPONSE,OUT_ONLY,ERROR_CODE,ERROR_MESSAGE,ERROR_DETAIL,ERROR_EXCEPTION,preserveProcessedHeaders"
			+ ",DISABLE_SMOOKS_RESULT_PAYLOAD,TRANSPORT_IN_NAME,SERVER_IP,FORCE_ERROR_ON_SOAP_FAULT";
	attrsInScope.put("default", "To,From,Action,FaultTo,ReplyTo,MessageID,MESSAGE_FORMAT,OperationName"
			                 + ",ERROR_CODE,ERROR_MESSAGE,ERROR_DETAIL,ERROR_EXCEPTION,"+GenericProperties);
	attrsInScope.put("transport", "");
	attrsInScope.put("axis2", "messageType,ContentType,,transportNonBlocking,disableAddressingForOutMessages,TRANSPORT_HEADERS,ClientApiNonBlocking");
	attrsInScope.put("axis2-clien", "");
	

}
}
