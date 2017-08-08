package SFDC2IDP.BASE.COMMON;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.ContentEncodingHttpClient;
import org.apache.poi.ss.formula.functions.T;

/**
 *
 * ���� commons-httpclient-3.1.jar commons-codec-1.4.jar
 * 
 * @author tianjun
 *
 */

public class Helper {
	public static String getArrayValue(String[] strings,Integer index) {
		 if(index < strings.length){
			 return strings[index];
		 }
		return null;

	}
	public static boolean isNotNull(String string) {
		  if(string==null || string.trim().equalsIgnoreCase("")){
			  return false;
		  }
		return true;

	}
	public static HashMap<String,SimpleDateFormat> dateformates = new HashMap<String,SimpleDateFormat>();
	public static String getTimeFlag(String datePattern) {
		SimpleDateFormat dateformate = null;
		if(dateformates.containsKey(datePattern)){
			dateformate =  dateformates.get(datePattern);
		}else{
			dateformate = new SimpleDateFormat(datePattern);
			dateformates.put(datePattern, dateformate);
		}
		
		return dateformate.format(new Date());
	}
	
	public static String getClipboardText() {
        Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();//��ȡϵͳ������
        // ��ȡ���а��е�����
        Transferable clipT = clip.getContents(null);
        String result = "";
        if (clipT != null) {
        // ��������Ƿ����ı�����
        if (clipT.isDataFlavorSupported(DataFlavor.stringFlavor))
			try {
				result = (String)clipT.getTransferData(DataFlavor.stringFlavor);
			} catch (UnsupportedFlavorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
        }
        return result;
    }
	
	public static String getRespond(String url) {
		String resultString = "";
		try {
			ContentEncodingHttpClient httpClient = new ContentEncodingHttpClient();
			HttpResponse response = httpClient.execute(new HttpGet(url));

			BufferedReader br = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			String line = "";

			while ((line = br.readLine()) != null) {
				resultString += line;

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return resultString;

	}

	public static void writerFile(String filePath, String content)
			 {
//		System.out.println(filePath);
		File resultFile = new File(filePath);
		if (!resultFile.getParentFile().exists()) {
			resultFile.getParentFile().mkdirs();
		}
		if (resultFile.exists()) {
			resultFile.delete();
		}
		try {
			resultFile.createNewFile();
			FileWriter fileWriter = new FileWriter(resultFile);
			fileWriter.append(content);
			fileWriter.flush();
			fileWriter.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}

	public static void main(String[] args) {
		System.out.println(Helper.getClipboardText());
	}
	
	public static String getClipOrFileContent(String filePath) throws Exception {
		// 2.2.��ȡģ���ļ�
		String result = "";
		result = Helper.getClipboardText();
		if(!Helper.isNotNull(result)){
			result = getFileContent(filePath);
		}
		
		return result;
	}

	private static HashMap<String, HashSet<String>> results = new HashMap<String, HashSet<String>>();

	public static HashSet<String> getFieldNamesFromSalesForceByObjectName(
			String objectName) {
		return getFieldNamesFromSalesForceByObjectName(objectName, false);
	}

	public static String getFileContent(String filePath,String LineSplit) throws Exception {
		// 2.2.��ȡģ���ļ�
		BufferedReader br = new BufferedReader(new FileReader(
				new File(filePath)));
		String templateString = "";
		String line = "";
		while ((line = br.readLine()) != null) {
			templateString += LineSplit + line;
		}
		
		br.close();
		return templateString = templateString.trim();
	}
	
	public static String getFileContent(String filePath) {
	
		try {
			return  getFileContent(filePath," ");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static String getFileContent(File file) {
		// 2.2.��ȡģ���ļ�
		String templateString = "";
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(file));
		
		
		String line = "";
		while ((line = br.readLine()) != null) {
			templateString += " " + line;
		}
		br.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return templateString = templateString.trim();
	}

	public static HashSet<String> getFieldNamesFromSalesForceByObjectName(
			String objectName, Boolean toLowerCase) {
		HashSet<String> result = new HashSet<String>();
		if (objectName == null || objectName.trim().equals("")) {
			return result;
		} else {
			objectName = objectName.trim().toLowerCase();
		}

		if (results.containsKey(objectName)) {
			return results.get(objectName);
		}
		try {
			String path = CONSTANTS.WSO2_SFDCObjectStruct_URL + objectName;
			ContentEncodingHttpClient httpClient = new ContentEncodingHttpClient();
			HttpResponse response = httpClient.execute(new HttpGet(path));
			System.out.println(Helper.class.getCanonicalName()
					+ "  StatusCode:"
					+ response.getStatusLine().getStatusCode());
			if (response.getStatusLine().getStatusCode() == 200) {
				System.out
						.println(Helper.class.getCanonicalName() + ":" + path);
				BufferedReader br = new BufferedReader(new InputStreamReader(
						response.getEntity().getContent()));
				String line = "";
				while ((line = br.readLine()) != null) {
					System.out.println(Helper.class.getCanonicalName()
							+ " Object Structure In Salesforce:" + line);
					if (toLowerCase) {
						line = line.toLowerCase();
					}
					result = dealWithString(line);
				}
				;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		results.put(objectName, result);

		return result;

	}

	// ɾ���ļ���Ŀ¼
	public static void clearFiles(Object file) {
		File f = null;
		if (file != null) {
			if (file instanceof File) {
				f = (File) file;
			} else {
				f = new File(file.toString());
			}
			if (f.exists()) {
				deleteFile(f);
			}
		}
	}

	private static void deleteFile(File file) {
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				deleteFile(files[i]);
			}
		}
		file.delete();
	}

	private static HashSet<String> dealWithString(String sql)
			throws IOException {
		HashSet<String> resultSet = new HashSet<String>();
		if (!sql.matches("(?i)Select\\sfrom")) {

			sql = sql.replaceFirst("(?i)<SOQL>", "").replaceFirst(
					"(?i)</SOQL>", "");
			String xmlString = "";

			sql = sql.trim();
			// System.out.println(sql);
			// ȥ�� select
			sql = sql.replaceFirst("^(?i)select\\s", "");
			// System.out.println(sql);
			// ��ȡ����
			String[] filesAtableName = sql.split("(?i)\\sfrom\\s");
			String tableName = filesAtableName[1].trim();
			// System.out.println(tableName);
			// ��ȡ�ֶ�ֵ
			String fields[] = filesAtableName[0].split(",");
			Arrays.sort(fields);
			// System.out.println(Arrays.toString(fields));
			for (String filed : fields) {
				resultSet.add(filed.trim());
			}
		}
		return resultSet;

	}
	public static String deleteLastChar(String result) {
		if(Helper.isNotNull(result)){
			return result.substring(0, result.length()-1);
		}
		return "";
	}
	public static String captureName(String name) {
		name = name.trim().toLowerCase();
		 char[] cs=name.toCharArray();
	     cs[0]-=32;
	     return String.valueOf(cs);
	}
}
