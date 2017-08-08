package zylon.sfdc2udp.object.main;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.poi.ss.usermodel.Cell;

import SFDC2IDP.BASE.COMMON.Helper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zylon.utils.Excel;
import com.zylon.utils.SelectSQLStruct;
import com.zylon.utils.WSO2;

import edu.emory.mathcs.backport.java.util.Arrays;

public class Main {
//	 private static Excel excel = new
//	 Excel("E:/lenovo-work/work/SFDC2UDP/≈ÌÓ˝/CPQ UDP BW Mapping AP PQ 0725.xlsx");
//	 private static String region = "ap";
	private static Excel excel = new Excel("E:/lenovo-work/work/SFDC2UDP/≈ÌÓ˝/CPQ UDP Mapping NA 0725.xlsx");
	private static String region = "na";

	public static void main(String[] args) {

		// ArrayList<Cell> cells =
		// excel.getColsByFirstColName("Èê¢ÁÜ∂È™áÈêúÓàöÓï®-ÈóàÁ?åeleteÁêõÔøΩ","sfdc_ms_la_account_idl");
		// for (Cell cell : cells) {
		// System.out.println(excel.getValue(cell));
		// }
		//

		File rootPath = new File(
				"E:/lenovo-work/work/SFDC2UDP/resources/soql archiver/sql/success/we_opportunity_product.sql");
		if (rootPath.isDirectory()) {
			for (File file : rootPath.listFiles()) {
				if (file.isDirectory()) {
					continue;
				}

				dealWith(file, excel);
			}
		} else {

			dealWith(rootPath, excel);
		}
	}

	private static String fileName = "";

	private static void dealWith(File file, Excel excel) {
		for (Entry<String, HashMap<String, ArrayList<Cell>>> sheets : excel.getSheets().entrySet()) {
			try {
				// System.out.println(sheets.getKey());
				ArrayList<Cell> objFields = excel.getColsByBlurFirstColName(sheets.getKey(), "SFDC Object Name");
				// System.out.println(objFields);
				ArrayList<Cell> csvFields = excel.getColsByBlurFirstColName(sheets.getKey(), "UDP table tst name");
				// System.out.println(csvFields);
				if (csvFields != null && objFields != null) {
					HashMap<String, String> result = getResult(objFields, csvFields);
				/*	System.out.println("SFDC2UDP_OBJECTS_SYNC_" + result.get("region").toUpperCase() + "_"
							+ result.get("objectRealName"));

					System.out.println("http://elb.wso2.lenovo.com:8080/esb/services/SFDC2UDP_OBJECTS_SYNC?region="
							+ result.get("region") + "&object=" + result.get("object"));
					System.out.println();
					String variables = "SFDC2UDP_OBJECTS_SYNC/variables/" + region + "/";

					 WSO2.write(variables+result.get("moduleName")+"/query.global.info.txt",
					 result.get("content"));
					WSO2.write(variables + result.get("moduleName") + "/query.global.latestdate.txt",
							"2017-07-01T00:00:00.000Z");
					WSO2.write(variables + result.get("moduleName") + "/queryLocator.txt", "null");
					
					*/
					//
					// try {
					// Thread.currentThread().sleep(3000);
					// } catch (InterruptedException e) {
					// // TODO Auto-generated catch block
					// e.printStackTrace();
					// }
					
					// if(fileds==null||fileds.length==0){
					// System.err.println(fileName+" can't get a success
					// response.");
					// return;
					// }
					//// for (int i = 0; i < fileds.length; i++) {
					//// String nameInExcel = excel.getValue(cells.get(i+6));
					//// String respondedNFieldName =
					// fileds[i].contains(":")?fileds[i].substring(3):fileds[i];
					//// if(!respondedNFieldName.equals("null")&&!respondedNFieldName.equalsIgnoreCase(nameInExcel)){
					//// System.err.println(i+" "+result.get("moduleName")+"
					// "+respondedNFieldName +" "+nameInExcel);
					//// }
					//// }
					// boolean haserror = false;
					// for (int i = 6; i < cells.size(); i++) {
					// String nameInExcel = excel.getValue(cells.get(i));
					// String respondedNFieldName = "";
					// try {
					// respondedNFieldName =
					// fileds[i-6].contains(":")?fileds[i-6].substring(3):fileds[i-6];
					// } catch (Exception e) {
					// }
					// if(!nameInExcel.equals("batch_number")&&!respondedNFieldName.equals("null")&&!respondedNFieldName.equalsIgnoreCase(nameInExcel)){
					// haserror = true;
					//// System.err.println(i+" "+result.get("moduleName")+"
					// "+respondedNFieldName +" "+nameInExcel);
					// }
					// }
					// if(!haserror){
					// System.err.println(fileName+".sql has run perfectly . ");
					// }
					// }else{
					// System.err.println("can not find fileds in excel about
					// "+fileName);

				}

	
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}

	private static String[] getFields(String result) {
		try {
			System.err.println("result : " + result.split("data_size")[1]);

		} catch (Exception e) {
			System.err.println("result : " + result);

		}
		return null;

	}

	private static HashMap<String, String> getResult(ArrayList<Cell> objFields, ArrayList<Cell> csvFields) {
		String nullPositonInNew = "";
		String batchNoPostionInNew = "-1";
		String keepLastModifiedDate = "true";
		String object = excel.getValue(objFields.get(0)).trim().split(":")[1].trim();
		String pathSource = excel.getValue(csvFields.get(0)).trim();
		String path = "";
	
		if (pathSource.contains(".")) {
			path = pathSource.split("\\.")[1].trim();
		} else {
			path = pathSource.split(":")[1].trim();
		}
		System.out.println(path);
		String sfdc_user_name = "";
		if (region.contains("ap")) {
			sfdc_user_name = "b2b.integration@lenovo.com.full";
		} else if (region.contains("na")) {
			sfdc_user_name = "cloud_integration@lenovo.com.na.full";
		}
		String fileName = object.replace("__c", "_" + region.toUpperCase());
		String batchCount = "300";
		// batchCount="1";
		
		StringBuffer stringBuff = new StringBuffer();

		stringBuff.append("{").append("\"region\":\"").append(region).append("\",").append("\n").append("\"object\":\"")
				.append(object).append("\",").append("\n").append("\"path\":\"").append(path).append("\",").append("\n")
				.append("\"fileName\":\"").append(fileName).append("\",").append("\n").append("\"batchCount\":\"")
				.append(batchCount).append("\",").append("\n").append("\"batch_no_position\":\"")
				.append(batchNoPostionInNew).append("\",").append("\n").append("\"null_position\":\"")
				.append(nullPositonInNew).append("\",").append("\n").append("\"keepLastModifiedDate\":\"")
				.append(keepLastModifiedDate).append("\",").append("\n").append("\"sfdc_user_name\":\"")
				.append(sfdc_user_name).append("\",").append("\n").append("\"sfdc_user_env\":\"")
				.append("test").append("\",").append("\n").append("\"sql\":\"")
				.append(createSelectSql(objFields, csvFields)).append("\"").append("\n").append("}");
		HashMap<String, String> result = new HashMap<String, String>();
		result.put("content", stringBuff.toString());
		result.put("moduleName", region.toLowerCase() + "_" + object.toLowerCase());
		result.put("region", region.toLowerCase());
		result.put("object", object.toLowerCase());
		result.put("objectRealName", object);

		return result;
	}

	private static Object createSelectSql(ArrayList<Cell> objFields, ArrayList<Cell> csvFields) {
		String sql = "select ";
		String objName = excel.getValue(objFields.get(0)).trim().split(":")[1].trim();

		for (int i = 1; i < objFields.size(); i++) {
			try {
				String fieldname = excel.getValue(objFields.get(i)).toLowerCase().replace(objName.toLowerCase() + ".", "").trim();

				if (fieldname.trim().equalsIgnoreCase("lastmodifiedby")
						|| fieldname.trim().equalsIgnoreCase("CreatedBy") || fieldname.trim().equalsIgnoreCase("owner")
						|| fieldname.trim().equalsIgnoreCase("recordtype")) {
					sql += fieldname + ".name,";
				} else if (fieldname.trim().equalsIgnoreCase("batch_num")) {

				} else {
					sql += fieldname + ",";
				}
			} catch (Exception e) {

			}
		}
		sql = Helper.deleteLastChar(sql);
		sql += " from " + objName;
		return sql.toLowerCase();
	}

}