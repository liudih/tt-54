package valueobjects.interaction;

import java.util.List;
import java.util.Map;

import valueobjects.product.IProductFragment;

public class InteractionProductMemberVideo implements IProductFragment{
	
	final List<dto.interaction.InteractionProductMemberVideo> productMembervoideos;
	final Map<String,String> cname;
	final String clistingid;
	
	public InteractionProductMemberVideo(List<dto.interaction.InteractionProductMemberVideo> productMembervideos,Map<String,String> cname,String clistingid){
		this.productMembervoideos=productMembervideos;
		this.cname=cname;
		this.clistingid=clistingid;
	}
	
	public List<dto.interaction.InteractionProductMemberVideo> getProductMembervideos()
	{
		return productMembervoideos;
	}
	
	public Map<String,String> getCname()
	{
		return cname;
	}

	public String getClistingid() {
		return clistingid;
	}

}
