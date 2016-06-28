package services.base.lang;

import interceptors.CacheResult;

import java.util.List;

import javax.inject.Inject;

import services.ILanguageService;
import valueobjects.base.Page;
import mapper.base.LanguageMapper;
import dto.Language;
import extensions.InjectorInstance;

public class LanguageService implements ILanguageService {

	@Inject
	LanguageMapper mapper;

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.base.lang.ILanguageService#getLanguage(int)
	 */
	@Override
	@CacheResult
	public Language getLanguage(int id) {
		return mapper.selectByPrimaryKey(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.base.lang.ILanguageService#getDefaultLanguage()
	 */
	@Override
	public Language getDefaultLanguage() {
		return mapper.selectDefaultLanguage();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.base.lang.ILanguageService#getLanguageMaxId()
	 */
	@Override
	public int getLanguageMaxId() {
		return mapper.getLanguageMaxId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.base.lang.ILanguageService#getAllLanguage()
	 */
	@Override
	public List<Language> getAllLanguage() {
		return mapper.getAllLanguage();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.base.lang.ILanguageService#getMaxLanguage(int)
	 */
	@Override
	public List<Language> getMaxLanguage(int mid) {
		return mapper.getMaxLanguage(mid);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.base.lang.ILanguageService#getAllSimpleLanguages()
	 */
	@Override
	public List<dto.SimpleLanguage> getAllSimpleLanguages() {
		return mapper.getAllSimpleLanguages();
	}

	public static ILanguageService getInstance() {
		return InjectorInstance.getInjector()
				.getInstance(LanguageService.class);
	}

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
	public Page<Language> getLanguagePage(int pageNo, int limit) {
		int pageIndex = (pageNo - 1) * limit;
		List<Language> languageList = mapper.getLanguages(pageIndex, limit);
		int count = mapper.getLanguageCount();
		return new Page<Language>(languageList, count, pageNo, limit);
	}

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
	public List<Language> getLanguagesByIds(List<Integer> languageIds) {
		return mapper.getLanguagesByIds(languageIds);
	}

	@CacheResult
	public Language getLanguageByCode(String langCode) {
		return mapper.getLanguageByCode(langCode);
	}
}
