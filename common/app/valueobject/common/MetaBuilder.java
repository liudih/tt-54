package valueobject.common;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MetaBuilder {

	String title;
	String description;
	List<String> keywords = new LinkedList<String>();
	Map<String, String> properties = new LinkedHashMap<String, String>();

	public MetaBuilder setTitle(String title) {
		this.title = title;
		return this;
	}

	public MetaBuilder setDescription(String description) {
		this.description = description;
		return this;
	}

	public MetaBuilder addKeyword(String keyword) {
		this.keywords.add(keyword);
		return this;
	}

	public MetaBuilder addProperty(String name, String content) {
		this.properties.put(name, content);
		return this;
	}

	public Meta build() {
		return new Meta(title, String.join(",", keywords), description,
				properties);
	}
}
