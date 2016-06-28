package common.test;

import java.util.Map;

import com.google.common.collect.Maps;

public class Helpers {

	public static Map<String, String> myBatisInMemoryDb(String... moduleNames) {
		Map<String, String> myBatisConfig = Maps.newHashMap();
		for (String moduleName : moduleNames) {
			myBatisConfig.put("mybatis." + moduleName + ".driver",
					"org.h2.Driver");
			myBatisConfig.put("mybatis." + moduleName + ".url", "jdbc:h2:mem:"
					+ moduleName);
			myBatisConfig.put("mybatis." + moduleName + ".user", "sa");
			myBatisConfig.put("mybatis." + moduleName + ".password", "");
		}
		return myBatisConfig;
	}
}
