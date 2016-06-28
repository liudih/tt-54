package valueobjects.product;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Final Product Object to be rendered as HTML.
 * 
 */
public class ProductComposite {

	final Map<String, IProductFragment> fragmentsMap = new HashMap<String, IProductFragment>();

	final Map<String, Object> attributes = new LinkedHashMap<String, Object>();

	final ProductContext productContext;

	public ProductComposite(ProductContext context) {
		this.productContext = context;
	}

	public void put(String name, IProductFragment fragment) {
		fragmentsMap.put(name, fragment);
	}

	public IProductFragment get(String name) {
		return fragmentsMap.get(name);
	}

	public Set<String> keySet() {
		return fragmentsMap.keySet();
	}

	public Map<String, IProductFragment> getFragmentsMap() {
		return fragmentsMap;
	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public ProductContext getProductContext() {
		return productContext;
	}

}
