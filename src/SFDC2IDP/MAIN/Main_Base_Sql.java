package SFDC2IDP.MAIN;
/**
 * ��Ҫע�������
 * 1.�ڵ��ýӿ�ʱ�������ǻ�ȡtoken�Ľӿڻ���ҵ��ӿڣ������ܻ����������⡣�����ڵ��ýӿ�ʧ�ܺ���������Ի��ơ�
 * ��������ÿ�����ʱ��������ԣ������ſ��������ܵ��ų�һЩ��ȷ�����������⡣��
 * 2.�ڵ��ýӿ�ʱ�����ܻ���ֻ�ȡ��token�ڵ��ýӿ�ʱ�պ�ʧЧ�������
 *   �����������������ǿ��Խ�token���ó�ȫ�ֵĳ������ڵ��ýӿ�ʧ�ܺ󣬶�
 *   token��ֵ����ˢ�£�Ȼ�����Ե��ýӿڡ������Ļ��������Խ��ͷ���wso2�ӿ�
 *   ��Ƶ�ʣ���ʡ���翪֧���ֿ��Ա���token�ڻ�ȡ���������ڵ������
 */
import SFDC2IDP.BASE.GENERATER.GenerateDSS_SQL;
import SFDC2IDP.BASE.GENERATER.GenerateMappingDMC;
import SFDC2IDP.BASE.GENERATER.GenerateMappingIn_Schema;
import SFDC2IDP.BASE.GENERATER.GenerateMappingOut_Schema;
import SFDC2IDP.BASE.GENERATER.GenerateProxy;
import SFDC2IDP.BASE.INTERFACE.IMappingHandler;
import SFDC2IDP.BASEIDPSQL.MAPPINGHANDLER.Handle_Mapping_BaseSQL;

public class Main_Base_Sql {
	public static void main(String[] args) throws Exception {
		IMappingHandler handle = Handle_Mapping_BaseSQL.getInstance();
//		new GenerateDSS_SQL(handle).execute();
		new GenerateMappingDMC(handle).execute();
		new GenerateMappingIn_Schema(handle).execute();
		new GenerateMappingOut_Schema(handle).execute();
		new GenerateProxy(handle).execute();
	}

}
