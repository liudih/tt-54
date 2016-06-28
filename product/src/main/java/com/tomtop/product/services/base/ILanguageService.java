package com.tomtop.product.services.base;

import java.util.List;

import com.tomtop.product.models.dto.base.Language;
import com.tomtop.product.models.dto.base.Page;
import com.tomtop.product.models.dto.base.SimpleLanguage;

public interface ILanguageService {

	public abstract Language getLanguage(int id);

	public abstract Language getDefaultLanguage();

	public abstract int getLanguageMaxId();

	public abstract List<Language> getAllLanguage();

	public abstract List<Language> getMaxLanguage(int mid);

	public abstract List<SimpleLanguage> getAllSimpleLanguages();

	/**
	 * 
	 * @Title: getLanguagePage
	 * @Description: TODO(查询语言（分页）)
	 * @param @param pageNo
	 * @param @param limit
	 * @param @return
	 * @return Page<Language>
	 * @throws
	 * @author yinfei
	 */
	public abstract Page<Language> getLanguagePage(int pageNo, int limit);

	/**
	 * 
	 * @Title: getLanguagesByIds
	 * @Description: TODO(通过语言ID列表查询语言列表)
	 * @param @param languageIds
	 * @param @return
	 * @return List<Language>
	 * @throws
	 * @author yinfei
	 */
	public abstract List<Language> getLanguagesByIds(List<Integer> languageIds);

	/**
	 * 获取语言
	 * 
	 * @param langCode
	 * @return
	 */
	public abstract Language getLanguageByCode(String langCode);

}