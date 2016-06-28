package valueobjects.product;

import java.util.List;
import valueobjects.product.IProductFragment;

public class AdvertisingProductDetail  implements IProductFragment {

	private List<AdItem> adDetailList;
	
	public AdvertisingProductDetail (List<AdItem> adDetailList){
		this.adDetailList = adDetailList;
	}
	
	public List<AdItem> getAdDetail(){
		return this.adDetailList;
	}
}
