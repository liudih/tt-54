package extensions.runtime.impl;

import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import com.google.common.collect.FluentIterable;

import extensions.runtime.IConfiguration;

public class PropertiesConfiguration implements IConfiguration {

	final Properties properties;

	public PropertiesConfiguration(Properties prop) {
		this.properties = prop;
	}

	@Override
	public IConfiguration getConfig(String key) {
		String prefix = key + ".";
		int len = prefix.length();
		List<String> matchKeys = FluentIterable.from(keys())
				.filter(s -> s.startsWith(prefix)).toList();
		List<SimpleEntry<String, String>> entries = FluentIterable
				.from(matchKeys)
				.transform(
						s -> new SimpleEntry<String, String>(s.substring(len),
								properties.getProperty(s))).toList();
		Properties p = new Properties();
		for (SimpleEntry<String, String> e : entries) {
			p.setProperty(e.getKey(), e.getValue());
		}
		return new PropertiesConfiguration(p);
	}

	@Override
	public String getString(String key) {
		return properties.getProperty(key);
	}

	@Override
	public Integer getInt(String key) {
		String s = properties.getProperty(key);
		return s != null ? Integer.parseInt(s) : null;
	}

	@Override
	public Integer getInt(String key, Integer def) {
		Integer i = getInt(key);
		return i != null ? i : def;
	}

	@Override
	public Long getLong(String key) {
		String s = properties.getProperty(key);
		return s != null ? Long.parseLong(s) : null;
	}

	@Override
	public Long getLong(String key, Long def) {
		Long l = getLong(key);
		return l != null ? l : def;
	}

	@Override
	public List<Object> getList(String key) {
		return FluentIterable.from(getStringList(key))
				.transform(s -> (Object) s).toList();
	}

	@Override
	public List<String> getStringList(String key) {
		String s = getString(key);
		if (s != null) {
			return Arrays.asList(s.split(","));
		}
		return null;
	}

	@Override
	public Set<String> keys() {
		return FluentIterable.from(properties.keySet())
				.transform(s -> s.toString()).toSet();
	}

}
