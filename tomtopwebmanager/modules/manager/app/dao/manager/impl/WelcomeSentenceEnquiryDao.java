package dao.manager.impl;

import interceptors.CacheResult;

import java.util.List;

import javax.inject.Inject;

import mapper.manager.WelcomeSentenceMapper;
import dao.manager.IWelcomeSentenceEnquiryDao;

public class WelcomeSentenceEnquiryDao implements IWelcomeSentenceEnquiryDao {
	@Inject
	WelcomeSentenceMapper mapper;

	@Override
	@CacheResult("welcomesentence")
	public List<entity.manager.WelcomeSentence> getWelcomeSentenceByLanguage(
			int languageId) {
		return mapper.getWelcomeSentenceByLanguage(languageId);
	}
}
