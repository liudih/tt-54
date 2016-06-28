package extensions.runtime;

import java.util.List;
import java.util.Set;

public interface IConfiguration {

	IConfiguration getConfig(String key);

	String getString(String key);

	Integer getInt(String key);

	Integer getInt(String key, Integer def);

	Long getLong(String key);

	Long getLong(String key, Long def);

	List<Object> getList(String key);

	List<String> getStringList(String key);

	Set<String> keys();

}
