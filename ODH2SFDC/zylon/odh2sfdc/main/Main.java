package zylon.odh2sfdc.main;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.poi.ss.usermodel.Cell;

import com.zylon.utils.Excel;
import com.zylon.utils.WSO2;

import SFDC2IDP.BASE.COMMON.Helper;

public class Main {
/**
 * sql=
 * select seqid,matnr,atnam,atwrt,action,inputtime 
 * from public.cto_bom_im
 *  where inputtime&gt;=:query_start_time 
 *  and seqid &gt;:query_start_id 
 *  order by seqid,inputtime 
 *  limit :query_offset
 * @param args
 */

	private static	Excel excel	= new  Excel("E:/lenovo-work/work/ODH_MTM_LISTPRICE_CHANNELPRICE_EMEA/PAO LBPIntegration_Interface Parameter_V3.3.xlsx");
 
	  public static void main(String[] args) {

		  for (Entry<String, HashMap<String, ArrayList<Cell>>> sheets : excel.getSheets().entrySet()) {
//				System.out.println(sheets.getKey());
				ArrayList<Cell>	odhFields = excel.getColsByBlurFirstColName(sheets.getKey(), "ODH Field");
//				System.out.println(objFields);
				ArrayList<Cell>	sfdcFields = excel.getColsByBlurFirstColName(sheets.getKey(), "SFDC Field");
//				ArrayList<Cell>	tableFields = excel.getColsByBlurFirstColName(sheets.getKey(), "ODH Table");
				
//				System.out.println(csvFields);
				if(sfdcFields!=null && odhFields!=null){
				    HashMap<String, String>	result	= getResult(odhFields,sfdcFields);
				
			
			}

			}
	}

	private static String[] getFields(String result) {
		 try {
				System.err.println("result : "+ result.split("payload_size")[1]);

		} catch (Exception e) {
			System.err.println("result : "+ result);

		}
			return null;
		
		
	}
/**
 * <xsl:value-of select="replace(dat:action,'&quot;','&quot;&quot;')"/>
								<xsl:text>","</xsl:text>
 * @param odhFields
 * @param sfdcFields
 * @return
 */
	private static HashMap<String, String> getResult(ArrayList<Cell> odhFields, ArrayList<Cell> sfdcFields) {
		
		
		  String content = "";
		
		for (int i = 1; i < odhFields.size(); i++) {
			
			  String odhField = excel.getValue(odhFields.get(i)).trim();
			  if(odhField.trim().equals("")){
				  continue;
			  }
			  String sfdcField  = excel.getValue(sfdcFields.get(i)).trim();
			  content+="<xsl:value-of select=\"replace(dat:"+odhField+",'&quot;','&quot;&quot;')\"/><xsl:text>\",\"</xsl:text>";
//			  if(!sfdcField.trim().equals("")){
//				  content+=" as "+sfdcField+",";
//			  }else{
//				  content+=",";
//			  }
		}
//		content = Helper.deleteLastChar(content);
//		content+=" from price_l_c_lbp_cpq_emea_ot  where seqid &gt;:query_start_id  order by seqid limit :query_offset";
//		
	  HashMap<String,String> result = new   HashMap<String,String> ();
		  result.put("content", content);
		System.out.println(content);
	     return result;
	}

	private static Object createSelectSql(ArrayList<Cell> objFields, ArrayList<Cell> csvFields) {
	String sql = "select ";
	String objName = excel.getValue(objFields.get(0)).trim().split(":")[1].trim();

	for (int i = 1; i < objFields.size(); i++) {
		try {
			String fieldname = excel.getValue(objFields.get(i)).trim().split("\\.",2)[1].trim();
			
			  if(fieldname.trim().equalsIgnoreCase("lastmodifiedby") || fieldname.trim().equalsIgnoreCase("CreatedBy")||fieldname.trim().equalsIgnoreCase("owner")||fieldname.trim().equalsIgnoreCase("recordtype")){
	        	  sql+=fieldname+".name,";
	          }else{
	        	  sql+=fieldname+",";
	          }
		} catch (Exception e) {

		}
	}
	sql =Helper.deleteLastChar(sql);
	sql+=" from "+objName;
		return sql.toLowerCase();
	}

}
