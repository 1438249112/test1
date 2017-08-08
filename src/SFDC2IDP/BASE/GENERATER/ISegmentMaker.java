package SFDC2IDP.BASE.GENERATER;

import java.util.HashMap;

public interface ISegmentMaker {

	public abstract void makeSegment(String tableName, String idpFiled,
			String idpFiledType, String nullString, boolean end);

	public abstract HashMap<String, String> getResults(); 

}