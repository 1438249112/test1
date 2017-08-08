import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.json.JSONArray;
import org.json.JSONObject;

import SFDC2IDP.BASE.COMMON.Helper;


/**
 <attributes>
                <meta>
                    <attribute defaultValue=""
                        expression="translate(get-property('MessageID'),':-','')"
                        name="MESSAGE_ID" type="STRING"
                        xmlns:ns="http://org.apache.synapse/xsd" xmlns:ns2="http://org.apache.synapse/xsd"/>
                </meta>
                <correlation>
                    <attribute defaultValue="" name="PROXY_ID"
                        type="INTEGER" value="1"/>
                    <attribute defaultValue="0" expression="$func:stats"
                        name="STATS" type="INTEGER"
                        xmlns:ns="http://org.apache.synapse/xsd" xmlns:ns2="http://org.apache.synapse/xsd"/>
                    <attribute defaultValue="200"
                        expression="get-property('axis2', 'HTTP_SC')"
                        name="STATS_CODE" type="INTEGER"
                        xmlns:ns="http://org.apache.synapse/xsd" xmlns:ns2="http://org.apache.synapse/xsd"/>
                    <attribute defaultValue="COMMON_AUDIT_LOG"
                        expression="$ctx:request.var.payload"
                        name="MEG_INPUT" type="STRING"
                        xmlns:ns="http://org.apache.synapse/xsd" xmlns:ns2="http://org.apache.synapse/xsd"/>
                    <attribute defaultValue="COMMON_AUDIT_LOG"
                        expression="$ctx:respondPayload"
                        name="MEG_OUTPUT" type="STRING"
                        xmlns:ns="http://org.apache.synapse/xsd" xmlns:ns2="http://org.apache.synapse/xsd"/>
                    <attribute defaultValue=""
                        expression="$func:backup1" name="BACKUP_1"
                        type="STRING"
                        xmlns:ns="http://org.apache.synapse/xsd" xmlns:ns2="http://org.apache.synapse/xsd"/>
                    <attribute defaultValue=""
                        expression="$func:backup2" name="BACKUP_2"
                        type="STRING"
                        xmlns:ns="http://org.apache.synapse/xsd" xmlns:ns2="http://org.apache.synapse/xsd"/>
                    <attribute defaultValue=""
                        expression="$func:backup3" name="BACKUP_3"
                        type="STRING"
                        xmlns:ns="http://org.apache.synapse/xsd" xmlns:ns2="http://org.apache.synapse/xsd"/>
                    <attribute defaultValue=""
                        expression="$func:backup4" name="BACKUP_4"
                        type="STRING"
                        xmlns:ns="http://org.apache.synapse/xsd" xmlns:ns2="http://org.apache.synapse/xsd"/>
                    <attribute defaultValue=""
                        expression="get-property('SYSTEM_TIME')"
                        name="CREATE_TIME" type="LONG"
                        xmlns:ns="http://org.apache.synapse/xsd" xmlns:ns2="http://org.apache.synapse/xsd"/>
                    <attribute defaultValue="0"
                        expression="$func:node_stats"
                        name="PROXY_NODE_NUMBER" type="INTEGER"
                        xmlns:ns="http://org.apache.synapse/xsd" xmlns:ns2="http://org.apache.synapse/xsd"/>
                </correlation>
                <payload>
                    <attribute defaultValue="" expression="$body"
                        name="VALUE" type="STRING"
                        xmlns:ns="http://org.apache.synapse/xsd" xmlns:ns2="http://org.apache.synapse/xsd"/>
                </payload>
                <arbitrary/>
            </attributes>
 */
public class Main {
private static String path = "E:/lenovo-work/work/AUDIT_LOG/log_struct.json";
public static void main(String[] args) {
	
	init();

    //创建一个xml文档
    Document doc = DocumentHelper.createDocument();
   
    //创建一个名为students的节点，因为是第一个创建，所以是根节点,再通过doc创建一个则会报错。
    Element root = doc.addElement("attributes");

try {
	
	 //通过字符串，获得最外部的json对象
    JSONObject jsonObj=new JSONObject(Helper.getFileContent(path));

for (Iterator iterator = jsonObj.keys(); iterator.hasNext();) {
	//获取payload/correlation等数组
	String key = iterator.next()+"";
	try {
		JSONArray arrayJson= jsonObj.getJSONArray(key);
		//解析数组
		String parentType = key.substring(0,key.length()-4);
		System.out.println(parentType+"..........");
		  Element stuEle = root.addElement(parentType);
		
		 for (int i = 0; i < arrayJson.length(); i++) {
			  Element sub = stuEle.addElement("attribute");
			 JSONObject arraySubObject =  arrayJson.getJSONObject(i);
			 String name = transfer(arraySubObject.get("name"));
			 String type = transfer(arraySubObject.get("type"));
			 sub.addAttribute("name", name);
			 sub.addAttribute("type", type);
			   addOtherAttribute(sub,name,type);
			    } 
	} catch (Exception e) {
		
	}
}
} catch (Exception e) {
	// TODO: handle exception
}
System.out.println(doc.asXML());
	       

}
private static HashMap<String,HashMap<String,String>> attributes =  new  HashMap<String,HashMap<String,String>> ();
private static void init() {
	attributes.put("payload", new HashMap<String,String>());
	attributes.get("payload").put("expression", "$body");
	attributes.get("payload").put("defaultValue", "");
	
	attributes.put("message_id", new HashMap<String,String>());
	attributes.get("message_id").put("expression", "translate(get-property('MessageID'),':-','')");
	attributes.get("message_id").put("defaultValue", "");
	
	
	attributes.put("log_no", new HashMap<String,String>());
	attributes.get("log_no").put("expression", "$func:log_no");
	attributes.get("log_no").put("defaultValue", "");
	
	
}
private static void addOtherAttribute(Element sub, String name, String type) {
 try {
	 HashMap<String, String>   attrs = attributes.get(name.trim().toLowerCase());
	 if(attrs==null || attrs.isEmpty()){
			sub.addAttribute("value", "0");
		}else{
			for (Entry<String, String> entry : attrs.entrySet()) {
				sub.addAttribute(entry.getKey(), entry.getValue());
			}
		}

	
} catch (Exception e) {
	// TODO: handle exception
}
	
	
}
private static String transfer(Object object) {
	try {
		if(object.toString().toLowerCase().contains("int")){
			return "INTEGER";
		}
	} catch (Exception e) {
		
	}
	return object.toString();
}
}
