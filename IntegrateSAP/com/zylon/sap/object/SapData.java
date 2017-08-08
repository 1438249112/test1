package com.zylon.sap.object;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class SapData implements Serializable{
	private static  HashMap<String, ArrayList<SapData>> dealedSapType = new  HashMap<String, ArrayList<SapData>>();
	private static  String  sapTypeDefinationFileRootPath = "E:/lenovo-work/SapDataDefination";
	 private String name = "";
	 private String type = "";
	 private String mandatory = "";
	 private String alias = "";
	 private String dataInOutType = "";
	 private String desc = "";
	 public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	private ArrayList<SapData> childSapDatas = new ArrayList<SapData>();
	 private File sapTypeDefinationRootPath = new File(sapTypeDefinationFileRootPath);
	public ArrayList<SapData> getChildSapDatas() {
		String type = this.type.trim().toLowerCase();
		if(type.length()<10){
			return childSapDatas;
		}
		if(dealedSapType.containsKey(type)){
			this.setChildSapDatas(dealedSapType.get(type));
		}else{
			initChildSapDatas(childSapDatas,type);
			dealedSapType.put(type, childSapDatas);
		}
//		       System.out.println(type);
		return childSapDatas;
	}
	private void initChildSapDatas(ArrayList<SapData> childSapDatas,
			String type) {
		
		for (File file : sapTypeDefinationRootPath.listFiles()) {
	
				BufferedReader br;
				try {
					br = new BufferedReader(new FileReader(file));
				
				String line = "";
				
				boolean finish = false;
				int i = 0 ;
				out:
				while ((line = br.readLine()) != null) {
					++i;
					if(i>=5){
//						System.out.println(i+":"+line);
						if(i==5){
							if(line.trim().toLowerCase().startsWith(type.trim().toLowerCase())){
//								System.err.println(line);
							}else{
								break out;
							}
						}else{
							SapData sapDate = new SapData();
							childSapDatas.add(sapDate);
							
							
							
						String[] values = line.trim().split("\\t");
						String name = values[0];
						if(name.contains(",")){
							sapDate.setName(name.split(",",2)[0]);
							sapDate.setAlias(name.split(",",2)[1]);
						}else{
							sapDate.setName(name);
							sapDate.setAlias(name.toLowerCase());
						}
						sapDate.setDesc(values[2]);
						sapDate.setDataInOutType(this.getDataInOutType());
						sapDate.setMandatory("Mandatory");
						sapDate.setType(values[18]+values[19]);
//						System.err.println(Arrays.toString(values));
						}
					}
					
					
				}
				
				br.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
	}
	public void setChildSapDatas(ArrayList<SapData> childSapDatas) {
		this.childSapDatas = childSapDatas;
	}
	public String getDataInOutType() {
		return dataInOutType;
	}
	public void setDataInOutType(String dataInOutType) {
		this.dataInOutType = dataInOutType;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getName() {
		return name;
	}
	public String getMandatory() {
		mandatory = mandatory.substring(0,1).toUpperCase();
		return mandatory;
	}
	public SapData(String name, String type, String mandatory) {
		super();
		this.name = name;
		this.type = type;
		this.mandatory = mandatory;
	}
	public void setMandatory(String mandatory) {
		this.mandatory = mandatory;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		if(type.toUpperCase().contains("CHAR")){
			try {
				type = "String("+type.replaceAll("\\s+", "").toUpperCase().replace("CHAR", "")+")";
				
			} catch (Exception e) {
				type =" String()";
			}
		}else{
			type = type.replaceAll("\\s+", "").toUpperCase();
		}
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public SapData() {
		
	}
 
}
