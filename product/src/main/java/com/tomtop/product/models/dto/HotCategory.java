package com.tomtop.product.models.dto;

import java.util.List;
import java.util.Map;

/**
 * 热卖品类
 * @author xcf
 *
 */
public class HotCategory {

	final Integer firstCategoryId;
	final String firstCategoryName;
	final String firstCategoryPath;
	final String imgLinkUrl;
	final Integer labelNameId;
	final String prompt;
	final List<Map<String, String>> secondCategoryNameAndPathMap;

	public HotCategory(Integer firstCategoryId, String firstCategoryName,
			String firstCategoryPath, String imgLinkUrl, Integer labelNameId,
			String prompt,
			List<Map<String, String>> secondCategoryNameAndPathMap) {
		super();
		this.firstCategoryId = firstCategoryId;
		this.firstCategoryName = firstCategoryName;
		this.firstCategoryPath = firstCategoryPath;
		this.imgLinkUrl = imgLinkUrl;
		this.labelNameId = labelNameId;
		this.prompt = prompt;
		this.secondCategoryNameAndPathMap = secondCategoryNameAndPathMap;
	}

	public String getImgLinkUrl() {
		return imgLinkUrl;
	}

	public Integer getLabelNameId() {
		return labelNameId;
	}

	public String getPrompt() {
		return prompt;
	}

	public Integer getFirstCategoryId() {
		return firstCategoryId;
	}

	public String getFirstCategoryName() {
		return firstCategoryName;
	}

	public String getFirstCategoryPath() {
		return firstCategoryPath;
	}

	public List<Map<String, String>> getSecondCategoryNameAndPathMap() {
		return secondCategoryNameAndPathMap;
	}

}
