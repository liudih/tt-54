package valueobjects.order_api.cart;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class CartComposite implements Serializable {
	private static final long serialVersionUID = 1L;
	final Map<String, ICartFragment> fragmentsMap = new HashMap<String, ICartFragment>();

	final Map<String, Object> attributes = new LinkedHashMap<String, Object>();

	final CartContext context;

	public CartComposite(CartContext context) {
		this.context = context;
	}

	public void put(String name, ICartFragment fragment) {
		fragmentsMap.put(name, fragment);
	}

	public ICartFragment get(String name) {
		return fragmentsMap.get(name);
	}

	public Set<String> keySet() {
		return fragmentsMap.keySet();
	}

	public Map<String, ICartFragment> getFragmentsMap() {
		return fragmentsMap;
	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public CartContext getCartContext() {
		return this.context;
	}
}
