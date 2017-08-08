package SFDC2IDP.BASE.COMMON;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
public class CreateSQLFileSolver {
	private HashMap<String,HashSet<String>> IDPFields = new  HashMap<String,HashSet<String>> ();
	private HashMap<String,HashSet<String>> IDPFieldsLowCase = new  HashMap<String,HashSet<String>> ();

	public HashMap<String, HashSet<String>> getIDPFieldsLowCase() {
		return IDPFieldsLowCase;
	}

	public void setIDPFieldsLowCase(
			HashMap<String, HashSet<String>> iDPFieldsLowCase) {
		IDPFieldsLowCase = iDPFieldsLowCase;
	}

	public static void main(String[] args) throws Exception {
		HashMap<String, HashSet<String>> results = new CreateSQLFileSolver().getIDPFields();
		System.out.println("success"+results);
	}

	public HashMap<String, HashSet<String>> getIDPFields() {
		return IDPFields;
	}

	public void setIDPFields(HashMap<String, HashSet<String>> iDPFields) {
		IDPFields = iDPFields;
	}

	public CreateSQLFileSolver() {

		try {
		
			BufferedReader br = new BufferedReader(new FileReader(new File(CONSTANTS.IDP_CreateTable_FilePath)));
			String createSqls = "";

			String line = "";
			while ((line = br.readLine()) != null) {
				createSqls += " " + line;
			}
			;
			createSqls = createSqls.trim();
			String[] arrayCreateSqls = createSqls.split("CREATE TABLE");
//			System.out.println(createSqls);
			for (String creatSqlString : arrayCreateSqls) {
				if(creatSqlString.length()>10){
					try {
						parseSQL(creatSqlString);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			// 去掉 select
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}

	private void parseSQL(String creatSqlString) {
		creatSqlString = creatSqlString.substring(0,
				creatSqlString.lastIndexOf("NULL")).trim();
		// 获取表名
		String tableName = creatSqlString.split("\\s")[0].trim();
		String suffix = "dbo.";
		if(tableName.startsWith(suffix)){
			tableName = tableName.substring(suffix.length());
		}
		System.out.println("table name = " + tableName);
		IDPFields.put(tableName, new HashSet<String>());
		IDPFieldsLowCase.put(tableName.trim().toLowerCase(), new HashSet<String>());
		
		// 获取字段
	
		String fieldPart = creatSqlString.substring(
				creatSqlString.indexOf("(") + 1, creatSqlString.length())
				.trim();

		String fields[] = fieldPart.split("NULL[^,]*,");
//		System.out.println("fields count = " + fields.length);
//		System.out.println("fields = " + Arrays.toString(fields));
		for (String field : fields) {
			String f = "";
			try {
				f =	field.trim().split("\\s")[0];
			} catch (Exception e) {
				// TODO: handle exception
			}
			if(!f.trim().equals("")){
				IDPFields.get(tableName).add(f.trim());
				IDPFieldsLowCase.get(tableName.trim().toLowerCase()).add(f.trim().toLowerCase());
			}
		}
	}
}