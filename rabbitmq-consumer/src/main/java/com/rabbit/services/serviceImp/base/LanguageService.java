package com.rabbit.services.serviceImp.base;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rabbit.conf.basemapper.LanguageMapper;
import com.rabbit.dto.base.LanguageLocal;
import com.rabbit.dto.base.SimpleLanguage;
@Service
public class LanguageService{

	@Autowired
	LanguageMapper mapper;

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.base.lang.ILanguageService#getLanguage(int)
	 */
	
	
	public LanguageLocal getLanguage(int id) {
		return mapper.selectByPrimaryKey(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.base.lang.ILanguageService#getDefaultLanguage()
	 */
	
	public LanguageLocal getDefaultLanguage() {
		return mapper.selectDefaultLanguage();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.base.lang.ILanguageService#getLanguageMaxId()
	 */
	
	public int getLanguageMaxId() {
		return mapper.getLanguageMaxId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.base.lang.ILanguageService#getAllLanguage()
	 */
	
	public List<LanguageLocal> getAllLanguage() {
		return mapper.getAllLanguage();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.base.lang.ILanguageService#getMaxLanguage(int)
	 */
	
	public List<LanguageLocal> getMaxLanguage(int mid) {
		return mapper.getMaxLanguage(mid);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.base.lang.ILanguageService#getAllSimpleLanguages()
	 */
	
	public List<SimpleLanguage> getAllSimpleLanguages() {
		return mapper.getAllSimpleLanguages();
	}

/*	public static ILanguageService getInstance() {
		return InjectorInstance.getInjector()
				.getInstance(LanguageService.class);
	}*/

	/*
	 * (non-Javadoc) <p>Title: getLanguagePage</p> <p>Description: 查询语言（分页）</p>
	 * 
	 * @param pageNo
	 * 
	 * @param limit
	 * 
	 * @return
	 * 
	 * @see services.ILanguageService#getLanguagePage(int, int)
	 */
	/*public Page<LanguageLocal> getLanguagePage(int pageNo, int limit) {
		int pageIndex = (pageNo - 1) * limit;
		List<LanguageLocal> languageList = mapper.getLanguages(pageIndex, limit);
		int count = mapper.getLanguageCount();
		return new Page<LanguageLocal>(languageList, count, pageNo, limit);
	}*/

	/*
	 * (non-Javadoc) <p>Title: getLanguagesByIds</p> <p>Description:
	 * 通过语言ID列表查询语言列表</p>
	 * 
	 * @param languageIds
	 * 
	 * @return
	 * 
	 * @see services.ILanguageService#getLanguagesByIds(java.util.List)
	 */
	public List<LanguageLocal> getLanguagesByIds(List<Integer> languageIds) {
		return mapper.getLanguagesByIds(languageIds);
	}

	
	public LanguageLocal getLanguageByCode(String langCode) {
		return mapper.getLanguageByCode(langCode);
	}
}
