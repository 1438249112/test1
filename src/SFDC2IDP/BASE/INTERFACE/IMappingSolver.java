package SFDC2IDP.BASE.INTERFACE;

import java.util.ArrayList;
import java.util.HashMap;

public interface IMappingSolver {

	public abstract HashMap<String, ArrayList<String>> getSFDCFields();

	public abstract void setSFDCFields(
			HashMap<String, ArrayList<String>> sFDCFields);

	public abstract HashMap<String, ArrayList<String>> getIDPFields();

	public abstract void setIDPFields(
			HashMap<String, ArrayList<String>> iDPFields);

	public abstract HashMap<String, ArrayList<String>> getIDPFieldTypes();

	public abstract void setIDPFieldTypes(
			HashMap<String, ArrayList<String>> iDPFieldTypes);

	public abstract void setTableNames(ArrayList<String> tableNames);

	public abstract HashMap<String, String> getTableName2ObjectName();

	public abstract void setTableName2ObjectName(
			HashMap<String, String> tableName2ObjectName);

	public abstract ArrayList<String> getTableNames();

}