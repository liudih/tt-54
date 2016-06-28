package valueobjects.loyalty;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import valueobjects.product.IProductFragment;
import valueobjects.product.ProductContext;

public class ThemeComposite {
	final Map<String, IThemeFragment> fragmentsMap = new HashMap<String, IThemeFragment>();

	final Map<String, Object> attributes = new LinkedHashMap<String, Object>();
	
	ThemeContext themeContext;
	
	public ThemeComposite(ThemeContext context) {
		this.themeContext = context;
	}

	public void put(String name, IThemeFragment fragment) {
		fragmentsMap.put(name, fragment);
	}

	public IThemeFragment get(String name) {
		return fragmentsMap.get(name);
	}

	public Set<String> keySet() {
		return fragmentsMap.keySet();
	}

	public Map<String, IThemeFragment> getFragmentsMap() {
		return fragmentsMap;
	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public ThemeContext getThemeContext() {
		return themeContext;
	}

	public void setThemeContext(ThemeContext themeContext) {
		this.themeContext = themeContext;
	}
}
