package ToSql;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.json.JSONArray;
import org.json.JSONObject;

import SFDC2IDP.BASE.COMMON.Helper;

public class Main {
private static String path = "C:/Users/litsoft/Desktop/test/sql.json";
public static void main(String[] args) {
String sql = "INSERT INTO MDS_ORDERS_ST ";
//System.out.println(sql);
//
//System.out.println(sql.replaceAll("\n", "qq123"));
String resouce = "{state__c=false, batchno=2017-02-17 11:36:40:416, createddate=2017-02-16 06:31:51:000, peer_session_id__c=001472038744000000629257, begintime__c=2016-08-24 03:40:48:000, recordfile_generatetme__c=, createdbyid=00528000005tpBsAAI, callduration__c=00:02:56, iscallplan__c=false, telnumber__c=07988513186, customername__c=工行江西省景德镇市分行, contactid__c=0032800000qbZGwAAM, linkmanid__c=1810922, ors_ip__c=10.99.223.33, id=a022800000UWGVKAA5, ivrsatisfyscore__c=, detailtime__c=2016-08-24 03:39:10:000, endtime__c=2016-08-24 03:43:44:000, groupid__c=DID_BJ, recordfilename__c=10.99.223.22_8838180_883818011910992232_132_20160824114051.mp3, recordrul__c=http://10.99.223.33/record_data/20160824/10.99.223.22_8838180_883818011910992232_132_20160824114051.mp3, lastmodifieddate=2017-02-17 02:22:25:000, pickupperiod__c=176, calltime__c=176, isritcode__c=598787259, customerid__c=0012800001A7lJ5AAJ, channel_state__c=0, file_exist__c=1, sessionid__c=001472038848000000629302, holdendtime__c=, onsitenumber__c=8838180, holdperiod__c=0, isconnection__c=true, bindingtype__c=false, cdbid__c=1111489, holdtime__c=, name=CL-00343404, lastmodifiedbyid=00528000005tpBsAAI, isavailibal__c=false, file_size__c=176688}";
//String resouce = "{so_warranty_status=In Warranty, trans_code=REP, so_so_close_time=2017-01-06 05:35:32, srv_customer_city=ADILABAD, so_esca_desc3=, so_esca_desc1=, so_created_time=2016-12-28 09:48:04, station_city=mancherial, so_esca_desc2=, scenario_name=L2 (HW replacement exclude Motherboard), srv_pops_purchase_date=, so_no=SOIN0630461612280005, so_trans_cp_send_time=, so_repair_type=Hardware with parts, so_reg_pickup_time=2017-01-06 04:35:27, srv_machines_sn=HB0P6AWG, so_maitrox_no=SOIN063046161228000500, id=257069451, so_t_tat=7, station_name=GANESH COMMUNICATIONS, so_parts_status_sum=Parts in service center, na_repair_desc=REPL LVL 2 Part, so_reg_carrier_name=, so_repair_lst_refid=X0V3X, station_id=046, vendor_name=Ensure, so_repair_end_time=2017-01-05 15:06:25, so_status=Closed, so_log_repairedby=Technician063046 , so_imei_new_imei2=, so_esca_status3=, so_imei_new_imei1=, so_repair_code=R0012, so_esca_status2=, srv_customer_phone=7386268560, update_date=2017-01-06 05:35:32, so_esca_status1=, srv_machines_imei=862315030417017, so_reg_sales_channel=, so_trans_cci_send_time=, so_esca_code1=, so_esca_code2=, so_repair_start_time=2016-12-29 06:03:02, key_account_name=, so_esca_code3=, so_r_tat=6, order_id=6516391, batch_number=20170118103921, so_reg_complaint_code=C0004, srv_machine_mtm=PA2M0018IN, srv_machine_type=PA2M, so_apply_time=2016-12-29 06:03:25, station_state=005, so_brand=VIBE, so_log_createdby=Reception063046 , srv_customer_address=MANCHERIAL ADILABAD, srv_machines_imei2=862315030417025, na_pb_desc=Camera, so_reg_doa=Not DOA, so_repair_old_ver=, so_cid=Not CID, so_arrival_time=2017-01-05 15:03:36, srv_machines_model=A6020a40, so_reg_track_no=, srv_customer_email=, station_country=IN, so_trans_cp_receive_time=, srv_customer_name=K YADAGIRI, so_trans_cci_receive_time=, so_log_closedby=Reception063046 , na_ntf=, so_repair_new_ver=A6020a40_S034_160923_ROW, so_reg_carry_in_time=2016-12-28 09:48:04, na_replace_part_no=1, so_imei_new_sn=, so_reg_complaint_desc=Camera Problem, so_parts_in_country=2016-12-29 06:03:25, so_repair_pb_code=P0007, srv_pops_active_date=}";
try {
	resouce = resouce.substring(1,resouce.length()-1);
	String xml = "";
   String keys= "";
   String values = "";
	String[] keyAvalues = resouce.split(",");
	for (String keyAvalueString : keyAvalues) {
		String[] keyAvalue = keyAvalueString.split("=");
		keys+=getArrayValue(keyAvalue,0)+",";
		values+="'"+getArrayValue(keyAvalue,1)+"',";
		xml+="<"+getArrayValue(keyAvalue,0).trim()+">"+getArrayValue(keyAvalue,1)+"</"+getArrayValue(keyAvalue,0).trim()+">";
	}
	keys = keys.substring(0,keys.length()-1);
	values = values.substring(0,values.length()-1);
	sql +=  "("+keys+") values ("+values+");";

	System.out.println(sql);
	System.out.println(xml);
} catch (Exception e) {
	e.printStackTrace();
}

}
private static String getArrayValue(String[] keyAvalue, int i) {
	try {
		return keyAvalue[i];
	} catch (Exception e) {
		// TODO: handle exception
	}
	return "";
}
}
