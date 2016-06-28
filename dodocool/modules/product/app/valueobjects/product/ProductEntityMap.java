package valueobjects.product;

import java.util.Map;

public class ProductEntityMap implements IProductFragment {

	final Map<String, String> entityMap;

	public ProductEntityMap(Map<String, String> entityMap) {
		this.entityMap = entityMap;
	}

	public Map<String, String> getEntityMap() {
		return entityMap;
	}

}
