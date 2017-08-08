package SFDC2IDP.BASE.INTERFACE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import SFDC2IDP.BASE.COMMON.CreateSQLFileSolver;

public interface IMappingHandler {

	public abstract HashMap<String, String> getMappingFraction();

	public abstract void setMappingFraction(
			HashMap<String, String> mappingFraction);

	public abstract CreateSQLFileSolver getSqlFileSolver();

	public abstract void setSqlFileSolver(CreateSQLFileSolver sqlFileSolver);

	public abstract HashSet<String> getObjectNames();

	public abstract void setObjectNames(HashSet<String> objectNames);

	public abstract HashMap<String, String> getSqlMap();

	public abstract void setSqlMap(HashMap<String, String> sqlMap);

	public abstract HashMap<String, String> getMapping_in_schema_Fraction();

	public abstract void setMapping_in_schema_Fraction(
			HashMap<String, String> mapping_in_schema_Fraction);

	public abstract HashMap<String, String> getMapping_out_schema_Fraction();

	public abstract void setMapping_out_schema_Fraction(
			HashMap<String, String> mapping_out_schema_Fraction);

	public abstract HashMap<String, String> getSoqlMap();

	public abstract void setSoqlMap(HashMap<String, String> soqlMap);

	public abstract ArrayList<String> getTableNames();

	public abstract void setTableNames(ArrayList<String> tableNames);

	public abstract HashMap<String, String> getTableName2ObjectName();

	public abstract void setTableName2ObjectName(
			HashMap<String, String> tableName2ObjectName);

	  abstract IMappingHandler execute();

}