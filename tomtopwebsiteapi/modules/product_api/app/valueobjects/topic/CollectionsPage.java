package valueobjects.topic;

import java.util.List;
import java.util.Map;

import dto.SimpleLanguage;
import dto.topic.TopicPage;

public class CollectionsPage {
	private List<String> types;
	private List<SimpleLanguage> slList;
	private Map<String, List<TopicPage>> map;
	private List<Integer> years;
	private Integer defaultLanguageId;

	public CollectionsPage(List<String> types, List<SimpleLanguage> slList,
			Map<String, List<TopicPage>> map, List<Integer> years,
			Integer defaultLanguageId) {
		this.types = types;
		this.slList = slList;
		this.map = map;
		this.years = years;
		this.defaultLanguageId = defaultLanguageId;
	}

	public List<SimpleLanguage> getSlList() {
		return slList;
	}

	public void setSlList(List<SimpleLanguage> slList) {
		this.slList = slList;
	}

	public Map<String, List<TopicPage>> getMap() {
		return map;
	}

	public void setMap(Map<String, List<TopicPage>> map) {
		this.map = map;
	}

	public List<Integer> getYears() {
		return years;
	}

	public void setYears(List<Integer> years) {
		this.years = years;
	}

	public Integer getDefaultLanguageId() {
		return defaultLanguageId;
	}

	public void setDefaultLanguageId(Integer defaultLanguageId) {
		this.defaultLanguageId = defaultLanguageId;
	}

	public List<String> getTypes() {
		return types;
	}

	public void setTypes(List<String> types) {
		this.types = types;
	}

}
