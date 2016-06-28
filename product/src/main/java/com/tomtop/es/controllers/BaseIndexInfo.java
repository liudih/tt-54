package com.tomtop.es.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.tomtop.es.common.BaseServiceUtil;
import com.tomtop.es.entity.AttributeItem;
import com.tomtop.es.entity.Constant;
import com.tomtop.es.entity.IndexEntity;
import com.tomtop.es.entity.Language;
import com.tomtop.es.entity.MutilLanguage;
import com.tomtop.es.entity.ProductEntity;
import com.tomtop.es.entity.ProductTypeEntity;
import com.tomtop.product.configuration.EsConfigSettings;

/**
 * 索引基础信息
 * 
 * @author ztiny
 */
@Service
public class BaseIndexInfo {

	Logger logger = Logger.getLogger(BaseIndexInfo.class);

	@Autowired
	private BaseServiceUtil baseUtil;

	@Autowired
	EsConfigSettings setting;

	// 局部更新索引时会用到
	public JSONObject route;
	public String[] indexAll;

	@PostConstruct
	public void init() {

		String routes = setting.getRoutes();
		this.route = JSONObject.parseObject(routes);

		String indexAllStr = setting.getProductAll();
		this.indexAll = indexAllStr.split(",");
	}

	/**
	 * 获取索引名称
	 * 
	 * @param product
	 * @return
	 */
	public List<String> getIndexNames() {
		List<String> languages = new ArrayList<String>();
		for (String index : this.indexAll) {
			String indexName = Constant.ES_INDEX_PREFIX + index;
			languages.add(indexName);
		}
		return languages;
	}

	/**
	 * 根据语言解析出不同版本的产品对象
	 * 
	 * @param product
	 * @return Map<Stirng,IndexEntity> 域名为键，值IndexEntity实体类
	 */
	public Map<String, IndexEntity> getMutilLanguagesProduct(
			ProductEntity product) throws Exception {
		if (product == null || product.getMutil() == null
				|| product.getMutil().size() < 1) {
			return null;
		}
		Map<String, Language> languageMap = baseUtil.getLanguageMap();
		// 解析product已经国际化的语言
		Map<String, MutilLanguage> domainsMap = getDomains(product);
		Map<String, IndexEntity> map = new HashMap<String, IndexEntity>();
		for (int i = 0; i < this.indexAll.length; i++) {
			List<String> urls = new ArrayList<String>();
			MutilLanguage mutil = null;
			IndexEntity indexModel = new IndexEntity();
			BeanUtils.copyProperties(product, indexModel, "mutil");
			// 判断产品是否已经国际化了该语言
			if (domainsMap.get(this.indexAll[i]) != null) {

				// 如果已经国际化了该语言，则取当前对象复制给索引对象
				mutil = domainsMap.get(this.indexAll[i]);
				if (mutil.getItems() == null || mutil.getItems().size() < 1) {
					MutilLanguage enMutil = domainsMap.get("en");
					mutil.setItems(enMutil.getItems());
				}

				if (StringUtils.isBlank(mutil.getTitle())
						&& !StringUtils.equals(this.indexAll[i], "en")) {
					MutilLanguage enMutil = domainsMap.get("en");
					mutil.setTitle(enMutil.getTitle());
				}

				if (StringUtils.isBlank(mutil.getDesc())
						&& !StringUtils.equals(this.indexAll[i], "en")) {
					MutilLanguage enMutil = domainsMap.get("en");
					mutil.setDesc(enMutil.getDesc());
				}

				if (StringUtils.isBlank(mutil.getKeywords())
						&& !StringUtils.equals(this.indexAll[i], "en")) {
					MutilLanguage enMutil = domainsMap.get("en");
					mutil.setKeywords(enMutil.getKeywords());
				}

				if (StringUtils.isBlank(mutil.getMetaDescription())
						&& !StringUtils.equals(this.indexAll[i], "en")) {
					MutilLanguage enMutil = domainsMap.get("en");
					mutil.setMetaDescription(enMutil.getMetaDescription());
				}

				if (StringUtils.isBlank(mutil.getMetaTitle())
						&& !StringUtils.equals(this.indexAll[i], "en")) {
					MutilLanguage enMutil = domainsMap.get("en");
					mutil.setMetaTitle(enMutil.getMetaTitle());
				}

				if (StringUtils.isBlank(mutil.getShortDescription())
						&& !StringUtils.equals(this.indexAll[i], "en")) {
					MutilLanguage enMutil = domainsMap.get("en");
					mutil.setShortDescription(enMutil.getShortDescription());
				}

				if (StringUtils.isBlank(mutil.getMetaKeyword())
						&& !StringUtils.equals(this.indexAll[i], "en")) {
					MutilLanguage enMutil = domainsMap.get("en");
					mutil.setMetaKeyword(enMutil.getMetaKeyword());
				}

				if (StringUtils.isBlank(mutil.getMetaKeyword())
						&& !StringUtils.equals(this.indexAll[i], "en")) {
					MutilLanguage enMutil = domainsMap.get("en");
					mutil.setMetaKeyword(enMutil.getMetaKeyword());
				}

				// 和类目匹配
				if (mutil.getProductTypes() == null
						|| mutil.getProductTypes().size() < 1) {
					MutilLanguage enMutil = domainsMap.get("en");
					mutil.setProductTypes(enMutil.getProductTypes());
				}

				if (mutil.getUrl() == null || mutil.getUrl().size() < 1) {
					MutilLanguage enMutil = domainsMap.get("en");
					mutil.setUrl(enMutil.getUrl());
				}

				mutil = getNewMutilLanguageByType(mutil, null);

				indexModel.setMutil(mutil);
			} else {
				// 如果没有则取英文站点国际化的属性
				mutil = domainsMap.get("en");
				if (mutil != null) {
					MutilLanguage newMutil = (MutilLanguage) mutil.clone();
					// 设置当前语言
					newMutil.setLanguageName(this.indexAll[i]);
					// 和属性匹配语言
					newMutil = getNewMutilLanguageByItem(newMutil, languageMap);
					// 和类目匹配
					newMutil = getNewMutilLanguageByType(newMutil, languageMap);

					if (newMutil.getUrl() != null) {
						urls.addAll(newMutil.getUrl());
					}
					indexModel.setMutil(newMutil);
				}
			}

			indexModel.getMutil().setUrl(handlerDoubleDate(urls));
			String indexName = Constant.ES_INDEX_PREFIX + this.indexAll[i];
			map.put(indexName, indexModel);
		}
		return map;
	}

	/**
	 * 根据语言解析出不同版本的产品对象
	 * 
	 * @param products
	 * @return
	 */
	public Map<String, List<IndexEntity>> getMutilLanguagesProducts(
			List<ProductEntity> products) throws Exception {
		if (products == null || products.size() < 1) {
			return null;
		}

		Map<String, Language> languageMap = baseUtil.getLanguageMap();
		Map<String, List<IndexEntity>> map = new HashMap<String, List<IndexEntity>>();
		for (ProductEntity productEntity : products) {
			List<String> urls = new ArrayList<String>();
			// 解析product已经国际化的语言
			Map<String, MutilLanguage> domainsMap = getDomains(productEntity);
			for (int i = 0; i < this.indexAll.length; i++) {
				IndexEntity indexModel = new IndexEntity();
				BeanUtils.copyProperties(productEntity, indexModel, "mutil");
				MutilLanguage mutil = null;
				// 判断是否有该语言的国际化属性
				if (domainsMap.get(this.indexAll[i]) != null) {
					// 如果已经国际化了该语言，则取当前对象复制给索引对象
					mutil = domainsMap.get(this.indexAll[i]);
					if (mutil.getItems() == null || mutil.getItems().size() < 1) {
						MutilLanguage enMutil = domainsMap.get("en");
						mutil.setItems(enMutil.getItems());
					}

					if (StringUtils.isBlank(mutil.getTitle())
							&& !StringUtils.equals(this.indexAll[i], "en")) {
						MutilLanguage enMutil = domainsMap.get("en");
						mutil.setTitle(enMutil.getTitle());
					}

					if (StringUtils.isBlank(mutil.getDesc())
							&& !StringUtils.equals(this.indexAll[i], "en")) {
						MutilLanguage enMutil = domainsMap.get("en");
						mutil.setDesc(enMutil.getDesc());
					}

					if (StringUtils.isBlank(mutil.getKeywords())
							&& !StringUtils.equals(this.indexAll[i], "en")) {
						MutilLanguage enMutil = domainsMap.get("en");
						mutil.setKeywords(enMutil.getKeywords());
					}

					if (StringUtils.isBlank(mutil.getMetaDescription())
							&& !StringUtils.equals(this.indexAll[i], "en")) {
						MutilLanguage enMutil = domainsMap.get("en");
						mutil.setMetaDescription(enMutil.getMetaDescription());
					}

					if (StringUtils.isBlank(mutil.getMetaTitle())
							&& !StringUtils.equals(this.indexAll[i], "en")) {
						MutilLanguage enMutil = domainsMap.get("en");
						mutil.setMetaTitle(enMutil.getMetaTitle());
					}

					if (StringUtils.isBlank(mutil.getShortDescription())
							&& !StringUtils.equals(this.indexAll[i], "en")) {
						MutilLanguage enMutil = domainsMap.get("en");
						mutil.setShortDescription(enMutil.getShortDescription());
					}

					if (StringUtils.isBlank(mutil.getMetaKeyword())
							&& !StringUtils.equals(this.indexAll[i], "en")) {
						MutilLanguage enMutil = domainsMap.get("en");
						mutil.setMetaKeyword(enMutil.getMetaKeyword());
					}

					if (StringUtils.isBlank(mutil.getMetaKeyword())
							&& !StringUtils.equals(this.indexAll[i], "en")) {
						MutilLanguage enMutil = domainsMap.get("en");
						mutil.setMetaKeyword(enMutil.getMetaKeyword());
					}

					// 和类目匹配
					if (mutil.getProductTypes() == null
							|| mutil.getProductTypes().size() < 1) {
						MutilLanguage enMutil = domainsMap.get("en");
						mutil.setProductTypes(enMutil.getProductTypes());
					}

					// 和类目匹配
					if (mutil.getUrl() == null || mutil.getUrl().size() < 1) {
						MutilLanguage enMutil = domainsMap.get("en");
						mutil.setUrl(enMutil.getUrl());
					}

					mutil = getNewMutilLanguageByType(mutil, null);

					// 取到当前对象的urls
					if (mutil.getUrl() != null) {
						urls.addAll(mutil.getUrl());
					}
					indexModel.setMutil(mutil);
				} else {
					// 如果没有则取英文站点国际化的属性
					mutil = domainsMap.get("en");
					if (mutil != null) {
						MutilLanguage newMutil = (MutilLanguage) mutil.clone();
						urls.addAll(newMutil.getUrl());
						// 设置当前语言
						newMutil.setLanguageName(this.indexAll[i]);
						newMutil.setLanguageId(languageMap
								.get(this.indexAll[i]).getId());
						// 和类目匹配
						newMutil = getNewMutilLanguageByType(newMutil,
								languageMap);
						if (newMutil.getUrl() != null) {
							urls.addAll(newMutil.getUrl());
						}
						indexModel.setMutil(newMutil);
					}
				}
				if (indexModel.getMutil() != null) {
					indexModel.getMutil().setUrl(handlerDoubleDate(urls));
				}
				String indexName = Constant.ES_INDEX_PREFIX + this.indexAll[i];
				// 同类型索引存放在同一集合里面
				List<IndexEntity> oldIndexs = map.get(indexName);
				if (oldIndexs == null) {
					oldIndexs = new ArrayList<IndexEntity>();
				}
				oldIndexs.add(indexModel);
				if (StringUtils.isNotBlank(indexName)) {
					map.put(indexName, oldIndexs);
				}
			}
		}
		return map;
	}

	public List<String> handlerDoubleDate(List<String> data) {
		List<String> newList = new ArrayList<String>();
		for (String string : data) {
			if (!newList.contains(string)) {
				newList.add(string);
			}
		}
		return newList;
	}

	/**
	 * 国际化对象匹配类目的国际化对象
	 * 
	 * @param mutil
	 * @return
	 */
	public MutilLanguage getNewMutilLanguageByType(MutilLanguage mutil,
			Map<String, Language> languageMap) {
		if (mutil == null) {
			return null;
		}
		List<ProductTypeEntity> types = mutil.getProductTypes();
		Language language = null;
		if (languageMap != null) {
			language = languageMap.get(mutil.getLanguageName());
		}
		List<ProductTypeEntity> models = new ArrayList<ProductTypeEntity>();
		for (ProductTypeEntity model : types) {
			if (language != null) {
				mutil.setLanguageId(language.getId());
			}
			// if (mutil.getLanguageId() == model.getLanguageId()) {
			// }
			models.add(model);
			mutil.setProductTypes(models);
		}
		return mutil;
	}

	/**
	 * 类目属性设置语言
	 * 
	 * @param mutil
	 * @param languageMap
	 * @param productTypes
	 * @param level
	 * @return
	 */
	public MutilLanguage getNewMutilLanguageByProductLevel(MutilLanguage mutil,
			Map<String, Language> languageMap,
			List<ProductTypeEntity> productTypes, int level) {
		if (mutil == null) {
			return null;
		}
		Language language = null;
		if (languageMap != null) {
			language = languageMap.get(mutil.getLanguageName());
		}
		List<ProductTypeEntity> models = new ArrayList<ProductTypeEntity>();
		for (ProductTypeEntity model : productTypes) {
			if (language != null) {
				mutil.setLanguageId(language.getId());
			}
			// if (mutil.getLanguageId() == model.getLanguageId()) {
			models.add(model);
			// }
			if (level == 1) {
				mutil.setProductLevel1(models);
			} else if (level == 2) {
				mutil.setProductLevel2(models);
			} else if (level == 3) {
				mutil.setProductLevel3(models);
			}
		}
		return mutil;
	}

	/**
	 * 国际化对象匹配多属性的国际化对象
	 * 
	 * @param mutil
	 * @return
	 */
	public MutilLanguage getNewMutilLanguageByItem(MutilLanguage mutil,
			Map<String, Language> languageMap) {
		if (mutil == null) {
			return null;
		}
		// 取当前站点的多属性对象
		List<AttributeItem> items = mutil.getItems();
		List<AttributeItem> newItems = new ArrayList<AttributeItem>();
		Language language = null;
		if (languageMap != null) {
			language = languageMap.get(mutil.getLanguageName());
		}
		for (AttributeItem attributeItem : items) {
			if (language != null) {
				mutil.setLanguageId(language.getId());
			}
			newItems.add(attributeItem);
			// if (mutil.getLanguageId() == attributeItem.getLanguageId()) {
			newItems.add(attributeItem);
			// }
			mutil.setItems(newItems);
		}
		return mutil;
	}

	/**
	 * 解析产品所有已经国际化的语言
	 * 
	 * @param product
	 * @return
	 */
	public Map<String, MutilLanguage> getDomains(ProductEntity product) {
		if (product == null || product.getMutil() == null) {
			return null;
		}
		List<MutilLanguage> mutils = product.getMutil();
		Map<String, MutilLanguage> map = new HashMap<String, MutilLanguage>();

		for (MutilLanguage mutilLanguage : mutils) {
			String domainName = mutilLanguage.getLanguageName();
			map.put(domainName, mutilLanguage);
		}
		return map;
	}

	/**
	 * 获取索引名称
	 * 
	 * @param languageName
	 *            国家域名缩写
	 * @return
	 */
	public String getIndexName(String languageName) {
		return Constant.ES_INDEX_PREFIX + languageName;
	}

	/**
	 * 获取索引名称
	 * 
	 * @param languageNames
	 *            国家域名缩写集合
	 * @return
	 */
	public List<String> getIndexName(List<String> languageNames) {
		if (languageNames == null || languageNames.size() < 1) {
			return null;
		}
		List<String> languages = new ArrayList<String>();
		for (String languageName : languageNames) {
			String indexName = getIndexName(languageName);
			languages.add(indexName);
		}
		return languages;
	}

	/**
	 * 字符串格式化之后，组装索引名称
	 * 
	 * @param languageName
	 *            语言名称,多个的话以逗号(,)号隔开
	 * @return
	 */
	public List<String> getDefaultIndexName(String languageName) {
		String[] languageNames = languageName.split(",");
		List<String> languages = new ArrayList<String>();
		for (String language : languageNames) {
			String indexName = getIndexName(language);
			languages.add(indexName);
		}
		return languages;
	}

}
