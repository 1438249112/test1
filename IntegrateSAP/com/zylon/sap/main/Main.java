package com.zylon.sap.main;

import java.io.File;

import SFDC2IDP.BASE.COMMON.Helper;


public class Main {
	public static String sapFuncResurePath = "E:/lenovo-work/work/KTC - direction to E-support portal  API Specificatio/source/functionSource/yl";

	public static String resultPath = "E:/lenovo-work/work/KTC - direction to E-support portal  API Specificatio/source/results/";

	public static void main(String[] args) {
		File rootPath = new File(sapFuncResurePath);
		for (File file : rootPath.listFiles()) {
			if(file.isDirectory()){
				continue;
			}
			Generator g =	 new Generator(file.getAbsolutePath());
//			 System.out.println(g.getReq_XSLT());
//			 System.out.println(g.getResp_XSLT());
		     String fileName = file.getName().substring(0,file.getName().lastIndexOf("."));
//			 Helper.writerFile(resultPath+fileName+".xslt",g.getReq_XSLT()+g.getResp_XSLT());
//			 Helper.writerFile(resultPath+fileName+"_sample.json",g.getReq_Sample()+g.getResp_Sample());
//			 Helper.writerFile(resultPath+fileName+"_fieldDefination.csv",g.getFieldDefinition());
        
			 Helper.writerFile(resultPath+fileName+"_schema.json",g.getSchema());
//           break;
		}
}
	
}
