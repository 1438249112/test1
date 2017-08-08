package com.zylon.sap.object;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

public class FuncFileParser {
	private  ExportDatas exportDatas = new ExportDatas();
	private  ImportDatas importDatas = new ImportDatas();
	private  ExportTableDatas exportTableDatas = new ExportTableDatas();
	private  ImportTableDatas importTableDatas = new ImportTableDatas();
	public ExportTableDatas getExportTableDatas() {
		return exportTableDatas;
	}
	public void setExportTableDatas(ExportTableDatas exportTableDatas) {
		this.exportTableDatas = exportTableDatas;
	}
	public ImportTableDatas getImportTableDatas() {
		return importTableDatas;
	}
	public void setImportTableDatas(ImportTableDatas importTableDatas) {
		this.importTableDatas = importTableDatas;
	}
	private  String funcName = "";
	private  String funcAlias = "";
	private  HashMap<String,String> returnTables = new  HashMap<String,String>();


	public HashMap<String, String> getReturnTables() {
		return returnTables;
	}
	
	public String getFuncAlias() {
		return funcAlias;
	}
	public void setFuncAlias(String funcAlias) {
		this.funcAlias = funcAlias;
	}
	public String getFuncName() {
		return funcName;
	}
	public void setFuncName(String funcName) {
		this.funcName = funcName;
	}
	public FuncFileParser(String sapFuncResurePath) {
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(
					new File(sapFuncResurePath)));
		
		String line = "";
		//dealflagÖµÎª £º null £¬ IMPORTING £¬EXPORTING
		String dealflag = "null";
		boolean finish = false;
		out:
		while ((line = br.readLine()) != null) {
			
			if(line.startsWith("FUNCTION")){
			String	funcName = line.substring("FUNCTION".length()+1, line.length()-1).trim().toUpperCase();
			if(funcName.contains(",")){
				this.setFuncName(funcName.split(",",2)[0]);
				this.setFuncAlias(funcName.split(",",2)[1]);
			}else{
				this.setFuncName(funcName);
				this.setFuncAlias(funcName);
			}
			};
			if(line.matches("\\*\"-+")){
				if(finish){
//					System.out.println("out");
					break out;
				}
				finish=true;
			}
		
		     if(line.matches("\\*\"\\s+\\w+")){
		    		 dealflag = line;
		    		 continue;
		     }
		 	if(dealflag.contains("IMPORTING")){
		 		importDatas.setSapDatas(line,"IMPORTING");
		     }
		 	if(dealflag.contains("EXPORTING")){
				exportDatas.setSapDatas(line,"EXPORTING");
		     }
		 	if(dealflag.contains("TABLES")){
//		 		System.out.println("exportTableDatas="+exportTableDatas.getIsInTableData());
//		 		System.out.println("importTableDatas="+importTableDatas.getIsInTableData());
		 		exportTableDatas.setSapDatas(line, "TABLES");
		 		importTableDatas.setSapDatas(line, "TABLES");
		     }
			
		}
		
		br.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public ExportDatas getExportDatas() {
		return exportDatas;
	}
	public void setExportDatas(ExportDatas exportDatas) {
		this.exportDatas = exportDatas;
	}
	public ImportDatas getImportDatas() {
		return importDatas;
	}
	public void setImportDatas(ImportDatas importDatas) {
		this.importDatas = importDatas;
	}
}
