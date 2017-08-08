package SFDC2IDP.MAIN;
/**
 * 主要注意的问题
 * 1.在调用接口时（无论是获取token的接口还是业务接口），可能会有网络问题。所以在调用接口失败后，最好有重试机制。
 * （重试最好可以延时几秒后重试，这样才可以最大可能的排除一些不确定的网络问题。）
 * 2.在调用接口时，可能会出现获取的token在调用接口时刚好失效的情况。
 *   对于这种情况，最好是可以将token设置成全局的常量，在调用接口失败后，对
 *   token的值进行刷新，然后重试调用接口。这样的话，即可以降低访问wso2接口
 *   的频率，节省网络开支，又可以避免token在获取后立即过期的情况。
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
