package extensions.runtime;

import java.util.List;
import java.util.Set;

import play.Configuration;

public class PlayConfiguration implements IConfiguration {

	Configuration config;

	public PlayConfiguration(Configuration configuration) {
		this.config = configuration;
	}

	@Override
	public IConfiguration getConfig(String key) {
		if (config.getConfig(key) != null) {
			return new PlayConfiguration(config.getConfig(key));
		}
		return null;
	}

	@Override
	public String getString(String key) {
		return config.getString(key);
	}

	@Override
	public Integer getInt(String key) {
		return config.getInt(key);
	}

	@Override
	public Long getLong(String key) {
		return config.getLong(key);
	}

	@Override
	public Integer getInt(String key, Integer def) {
		return config.getInt(key, def);
	}

	@Override
	public Long getLong(String key, Long def) {
		return config.getLong(key, def);
	}

	@Override
	public List<Object> getList(String key) {
		return config.getList(key);
	}

	@Override
	public List<String> getStringList(String key) {
		return config.getStringList(key);
	}

	@Override
	public Set<String> keys() {
		return config.keys();
	}

}
