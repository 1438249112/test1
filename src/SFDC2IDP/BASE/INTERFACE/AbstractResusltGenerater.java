package SFDC2IDP.BASE.INTERFACE;


public class AbstractResusltGenerater {
	protected IMappingHandler handle_SFDC2IDP_Mapping;

	public AbstractResusltGenerater(
			IMappingHandler handle_SFDC2IDP_Mapping) {
		this.handle_SFDC2IDP_Mapping = handle_SFDC2IDP_Mapping;
	}
	public AbstractResusltGenerater() {
		this.handle_SFDC2IDP_Mapping = handle_SFDC2IDP_Mapping;
	}
}