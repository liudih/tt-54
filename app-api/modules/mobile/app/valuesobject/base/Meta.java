package valuesobject.base;

import java.util.HashMap;
import java.util.Map;

/**
 * 代表HTML里的META字段与Title等，作为页面显示的参数封装。
 * 
 * @author kmtong
 *
 */
public class Meta {

	final String title;

	final String keywords;

	final String description;

	final Map<String, String> properties;

	public Meta(String title, String keywords, String description,
			Map<String, String> properties) {
		super();
		this.title = title;
		this.keywords = keywords;
		this.description = description;
		this.properties = (properties == null) ? new HashMap<String, String>()
				: properties;
	}

	/**
	 * Just enough for initial testing/compilation purpose
	 */
	public Meta() {
		this(null, null, null, null);
	}

	public String getTitle() {
		return title;
	}

	public String getKeywords() {
		return keywords;
	}

	public String getDescription() {
		return description;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperty(String name, String value) {
		properties.put(name, value);
	}

	public String getProperty(String name) {
		return properties.get(name);
	}
}
