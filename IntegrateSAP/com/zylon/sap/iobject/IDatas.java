package com.zylon.sap.iobject;

import java.util.ArrayList;

import SFDC2IDP.BASE.COMMON.Helper;

import com.zylon.sap.object.SapData;

import edu.emory.mathcs.backport.java.util.Arrays;

public class IDatas {
	private Boolean isInTableData = false;


	public void setIsInTableData(Boolean isInTableData) {
		this.isInTableData = isInTableData;
	}

	private ArrayList<SapData> sapDatas = new ArrayList<SapData>();

	public ArrayList<SapData> getSapDatas() {
		return sapDatas;
	}

	public void setSapDatas(ArrayList<SapData> sapDatas) {
		this.sapDatas = sapDatas;
	}

	public void setSapDatas(String sapDatas, String dataInOutType) {
		// *" VALUE(STATE) TYPE REGIO OPTIONAL
		SapData sapDate = new SapData();
		String[] datas = sapDatas.replace("*\"", "").trim().split("\\s+");
		if (dataInOutType.contains("TABLES")) {
//			System.out.println(isInTableData);
			if (datas[0].trim().equalsIgnoreCase("in") && isInTableData) {
//				System.out.println("in");
				setDataToSapDate(datas,sapDate,true);
				
			} else if (datas[0].trim().equalsIgnoreCase("out")
					&& !isInTableData) {
//				System.out.println("out");
				setDataToSapDate(datas,sapDate,true);
			} else if (!datas[0].trim().equalsIgnoreCase("out")
					&& !datas[0].trim().equalsIgnoreCase("in")
					&& !isInTableData) {
//				System.out.println("out");
				setDataToSapDate(datas,sapDate,false);
			} else {
//				System.out.println("break");
				return;
			}
			// System.out.println(Arrays.toString(datas));
		} else {

			// System.out.println(datas[0].substring(6,datas[0].length()-1));
			String name = datas[0].substring(6, datas[0].length() - 1);
			if (name.contains(",")) {
				sapDate.setName(name.split(",", 2)[0]);
				sapDate.setAlias(name.split(",", 2)[1]);
			} else {
				sapDate.setName(name);
				sapDate.setAlias(name.trim().toLowerCase());
			}

			sapDate.setType(Helper.getArrayValue(datas, 2));
			String mandatory = Helper.getArrayValue(datas, 3);
			if (Helper.isNotNull(mandatory)) {
				sapDate.setMandatory(mandatory.toLowerCase());
			} else {
				sapDate.setMandatory("Mandatory");
			}
		}
		sapDate.setDataInOutType(dataInOutType);

		this.sapDatas.add(sapDate);
	}

	private void setDataToSapDate(String[] datas, SapData sapDate,boolean containsInOutFlag) {
		int index = -1;
		if(containsInOutFlag){
			 index = 0;
		}else{
			 index = -1;
		}
		String name = datas[index+1];
		if (name.contains(",")) {
			sapDate.setName(name.split(",", 2)[0]);
			sapDate.setAlias(name.split(",", 2)[1]);
		} else {
			sapDate.setName(name);
			sapDate.setAlias(name.trim().toLowerCase());
		}
		
		sapDate.setType(Helper.getArrayValue(datas, index+3));
		String mandatory = Helper.getArrayValue(datas, index+4);
		if (Helper.isNotNull(mandatory)) {
			sapDate.setMandatory(mandatory.toLowerCase());
		} else {
			sapDate.setMandatory("Mandatory");
		}
	}

}
