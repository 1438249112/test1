package com.zylon.sap.main;

import SFDC2IDP.BASE.COMMON.Helper;

import com.zylon.sap.object.ExportDatas;
import com.zylon.sap.object.FuncFileParser;
import com.zylon.sap.object.ImportDatas;
import com.zylon.sap.object.SapData;

public class Generator {
	private static String req_template_path = "E:/lenovo-work/work/KTC - direction to E-support portal  API Specificatio/source/wso2resoure/template/request_template.xslt";
	private static String reps_template_path = "E:/lenovo-work/work/KTC - direction to E-support portal  API Specificatio/source/wso2resoure/template/response_template.xslt";
	private static String reps_path = "E:/lenovo-work/work/KTC - direction to E-support portal  API Specificatio/source/wso2resoure/template/response.xslt";
	private FuncFileParser funcFileParser = null;
	public static String xslt_mapping_template_request = "<field name=\"${SAPField}\"> <xsl:value-of select=\"//${RequestField}/child::text()\"/> </field>";
	public static String xslt_mapping_template_response = "<${responseFieldName}> <xsl:value-of select=\"//OUTPUT/${SAPField}/text()\"/> </${responseFieldName}>";
	public static String xslt_mapping_template_response_for_foreach = "<${responseFieldName}> <xsl:value-of select=\"${SAPField}/text()\"/> </${responseFieldName}>";
	public static String response_josn_template = "{ \"response\": { \"header\": { \"status\": \"SUCCESS\", \"status_code\": \"001\","
			+ " \"status_desc\": \"Successfully received sample data\", \"msg_id\": \"urn:uuid:37a2e1ce-692c-472f-a25b-0464b566cb89\", "
			+ "\"req_msg_id\": \"urn:uuid:37a2e1ce-692c-472f-a25b-0464b566cb89\" },"
			+ " \"data\":" + " { ${outputjson} } } } ";
	public static String response_josn = "{ \"response\": { \"header\": { \"status\": \"SUCCESS\", \"status_code\": \"001\","
			+ " \"status_desc\": \"Successfully received sample data\", \"msg_id\": \"urn:uuid:37a2e1ce-692c-472f-a25b-0464b566cb89\", "
			+ "\"req_msg_id\": \"urn:uuid:37a2e1ce-692c-472f-a25b-0464b566cb89\" } } } ";
	public static String schemaTemplate = "{ \"$schema\": \"http://json-schema.org/draft-04/schema#\", \"type\": \"object\", \"properties\": "
			+ "{ \"request\": { \"type\": \"object\", \"properties\":"
			+ " { ${fields} }, \"required\": [${requiredfield}] }"
			+ " }, \"required\": [\"request\"] } ";
	public FuncFileParser getFuncFileParser() {
		return funcFileParser;
	}

	public void setFuncFileParser(FuncFileParser funcFileParser) {
		this.funcFileParser = funcFileParser;
	}

	public String getReq_Sample() {
		return req_Sample;
	}

	public void setReq_Sample(String req_Sample) {
		this.req_Sample = req_Sample;
	}

	public String getResp_Sample() {
		return resp_Sample;
	}

	public void setResp_Sample(String resp_Sample) {
		this.resp_Sample = resp_Sample;
	}

	public String getReq_XSLT() {
		return req_XSLT;
	}

	public void setReq_XSLT(String req_XSLT) {
		this.req_XSLT = req_XSLT;
	}

	public String getResp_XSLT() {
		return resp_XSLT;
	}

	public void setResp_XSLT(String resp_XSLT) {
		this.resp_XSLT = resp_XSLT;
	}

	private String req_XSLT = "";
	private String resp_XSLT = "";
	private String req_Sample = "";
	private String resp_Sample = "";
	private String fieldDefinition = "";
	private String schema = "";
	
	public String getFieldDefinition() {
		return fieldDefinition;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public void setFieldDefinition(String fieldDefinition) {
		this.fieldDefinition = fieldDefinition;
	}

	public Generator(String sapFuncResurePath) {
		funcFileParser = new FuncFileParser(sapFuncResurePath);

		req_XSLT = generateReqXSLT(funcFileParser, req_template_path);

		resp_XSLT = generateRespXSLT(funcFileParser, reps_template_path,reps_path);
	
		req_Sample = generateReqSample(funcFileParser);
		resp_Sample = generateRespSample(funcFileParser);
		
		fieldDefinition = generateFieldDefinitions(funcFileParser);
//		System.out.println(fieldDefinition);
		schema = generateSchema(funcFileParser);

		  for (SapData iterable_element : funcFileParser.getImportTableDatas().getSapDatas()) {
//			  System.out.println(iterable_element.getChildSapDatas());
//			  System.out.println(iterable_element.getType());
			}
		  for (SapData iterable_element : funcFileParser.getExportTableDatas().getSapDatas()) {
//			  System.out.println(iterable_element.getChildSapDatas());
//			  System.out.println(iterable_element.getType());
			}
	}

	private String generateSchema(FuncFileParser funcFileParser2) {
       String fields = "";
       String requiredfields = "";
        for (SapData sapData : funcFileParser2.getImportDatas().getSapDatas()) {
//        	\"iv_flag\"
        	fields+="\""+sapData.getAlias().toLowerCase().trim()+"\": { \"type\": \"string\" },";
        	System.err.println(sapData.getAlias().toLowerCase().trim()+sapData.getMandatory());
        	if(sapData.getMandatory().trim().contains("M")){
        		requiredfields+="\""+sapData.getAlias().toLowerCase().trim()+"\",";
        	}
        	
		}
        requiredfields = Helper.deleteLastChar(requiredfields);
        fields = Helper.deleteLastChar(fields);
        return this.schemaTemplate.replace("${fields}", fields).replace("${requiredfield}", requiredfields);
		
	}

	private String generateFieldDefinitions(FuncFileParser parser) {
		String result = "REQUEST\nField Name,Field XPath,Field Type/length,Mandatory/Optional,Comments\n";
		 for (SapData sapData : parser.getImportDatas().getSapDatas()) {
			 result+=genFieldName(sapData.getName())+","+"/request/"+sapData.getAlias()+","+sapData.getType()+","+sapData.getMandatory()+"\n";
			}
		 for (SapData sapData : parser.getImportTableDatas().getSapDatas()) {
				for (SapData sapChildData : sapData.getChildSapDatas()) {
					 result+=genFieldName(sapChildData.getName())+","+"/request/"+sapData.getAlias()+"[]/"+sapChildData.getAlias()+","+sapChildData.getType()+","+sapChildData.getMandatory()+"\n";
				}
			
			}
		 
		 result += "RRESPONSE\nField Name,Field XPath,Field Type/Length,Mandatory/Optional,Comments\n";
		 for (SapData sapData : parser.getExportDatas().getSapDatas()) {
			 result+= genFieldName(sapData.getName())+","+"/response/data/"+sapData.getAlias()+","+sapData.getType()+","+sapData.getMandatory()+"\n";
			}
		  for (SapData sapData : parser.getExportTableDatas().getSapDatas()) {
			
				for (SapData sapChildData : sapData.getChildSapDatas()) {
					 result+=genFieldName(sapChildData.getName())+","+"/response/data/"+sapData.getAlias()+"[]/"+sapChildData.getAlias()+","+sapChildData.getType()+","+sapChildData.getMandatory()+"\n";
				}
				
			}
		  
		return result;
	}

	private String genFieldName(String name) {
		String result = "";
		if(Helper.isNotNull(name)){
			  for (String value : name.split("_")) {
					if(value.length()>1){
						result+=Helper.captureName(value)+" ";
					}
				}
		}
		
		return Helper.deleteLastChar(result); 
	
	}

	private String generateRespSample(FuncFileParser parser) {
		String result = "";
		ExportDatas importdatas = parser.getExportDatas();
		for (SapData sapData : importdatas.getSapDatas()) {
			result += "\"" + sapData.getAlias().toLowerCase() + "\":" + "\""
					 + "xxx\",";
		}
		
		 for (SapData sapData : parser.getExportTableDatas().getSapDatas()) {
			  result += "\""+sapData.getAlias()+"\":[{";
				for (SapData sapChildData : sapData.getChildSapDatas()) {
					result += "\"" + sapChildData.getAlias().toLowerCase() + "\":\"xxx\",";
				}
				result = Helper.deleteLastChar(result);
			    result += "}],";
			}
		 
		 
		result = Helper.deleteLastChar(result);
		if (Helper.isNotNull(result)) {
			return response_josn_template.replace("${outputjson}", result);
		} else {
			return response_josn;
		}

	}

	private String generateReqSample(FuncFileParser parser) {
		String result = "{\"request\":{";
		ImportDatas importdatas = parser.getImportDatas();
		for (SapData sapData : importdatas.getSapDatas()) {
			result += "\"" + sapData.getAlias().toLowerCase() + "\":\"xxx\",";
		}
		  for (SapData sapData : parser.getImportTableDatas().getSapDatas()) {
			  result += "\""+sapData.getAlias()+"\":[{";
				for (SapData sapChildData : sapData.getChildSapDatas()) {
					result += "\"" + sapChildData.getAlias().toLowerCase() + "\":\"xxx\",";
				}
				result = Helper.deleteLastChar(result);
			    result += "}]";
			}
		  result = Helper.deleteLastChar(result);

		result += "}}";
		return result;
	}
//////test//////////////
	private String generateReqXSLT(FuncFileParser parser,
			String req_template_path) {
		// request
		String req_templateContent = Helper.getFileContent(req_template_path);

		String req_template = xslt_mapping_template_request;
		String templatString = "";
		  for (SapData sapData : parser.getImportTableDatas().getSapDatas()) {
			  templatString += "";
				for (SapData sapChildData : sapData.getChildSapDatas()) {
					templatString += req_template.replace("${SAPField}",
							sapChildData.getName().toUpperCase()).replace("${RequestField}",
							sapData.getAlias().toLowerCase()+"/"+sapChildData.getAlias().toLowerCase());
				}
			}

			for (SapData sapData : parser.getImportDatas().getSapDatas()) {
				templatString += req_template.replace("${SAPField}",
						sapData.getName().toUpperCase()).replace("${RequestField}",
								sapData.getAlias().toLowerCase());
				for (SapData sapChildData : sapData.getChildSapDatas()) {
					templatString += req_template.replace("${SAPField}",
							sapData.getName().toUpperCase()).replace("${RequestField}",
							sapData.getAlias().toLowerCase()+"/"+sapChildData.getAlias().toLowerCase());
				}
			}
		return req_templateContent.replace("sapFuncName", parser.getFuncName())
				.replace("<template_content/>", templatString);
	}

	private String generateRespXSLT(FuncFileParser parser,
			String reps_template_path,String reps_path) {
		String templatString = "";
		// response

		String resp_template = xslt_mapping_template_response;
		String resp_template_for_foreach = xslt_mapping_template_response_for_foreach;
		
		  for (SapData sapData : parser.getExportTableDatas().getSapDatas()) {
			  templatString += " <xsl:for-each select=\"//TABLES/"+sapData.getName()+"/item\"><"+sapData.getAlias()+">";
				for (SapData sapChildData : sapData.getChildSapDatas()) {
					templatString += resp_template_for_foreach.replace("${SAPField}",sapChildData.getName()).replace("${responseFieldName}",
							sapChildData.getAlias());
				}
				 templatString += "</"+sapData.getAlias()+"> </xsl:for-each>";
			}
		  
		for (SapData sapData : parser.getExportDatas().getSapDatas()) {
			templatString += resp_template.replace("${SAPField}",
					sapData.getName().toUpperCase()).replace(
					"${responseFieldName}", sapData.getAlias().toLowerCase());
		
		}
		if(Helper.isNotNull(templatString)){
			String reps_templateContent = Helper.getFileContent(reps_template_path);
			return reps_templateContent.replace("<template_content/>",
					templatString);
		}else{
			return  Helper.getFileContent(reps_path);
		}
			
	}
}
