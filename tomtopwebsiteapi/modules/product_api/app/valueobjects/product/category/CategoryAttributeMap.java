package valueobjects.product.category;

import java.util.LinkedHashMap;

public class CategoryAttributeMap {
	private LinkedHashMap<Integer, LinkedHashMap<String, LinkedHashMap<Integer, String>>> msHashMap;

	public LinkedHashMap<Integer, LinkedHashMap<String, LinkedHashMap<Integer, String>>> getMsHashMap() {
		return msHashMap;
	}

	public void setMsHashMap(
			LinkedHashMap<Integer, LinkedHashMap<String, LinkedHashMap<Integer, String>>> msHashMap) {
		this.msHashMap = msHashMap;
	}

	public CategoryAttributeMap(LinkedHashMap<Integer, LinkedHashMap<String, LinkedHashMap<Integer, String>>> msHashMap) {
		setMsHashMap(msHashMap);
	}

	
}
