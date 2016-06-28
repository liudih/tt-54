package extensions.manager;

import java.util.List;

import javax.inject.Inject;

import play.Logger;

import com.google.common.collect.Lists;

import dao.manager.IWelcomeSentenceEnquiryDao;
import extensions.livechat.topic.WelcomeSentence;

public class WelcomeSentenceProvider implements
		extensions.livechat.topic.IWelcomeSentenceProvider {

	@Inject
	IWelcomeSentenceEnquiryDao welcomeSentenceEnquiryDao;

	@Override
	public List<WelcomeSentence> getWelcomeSentence(int langaugeId) {
		List<entity.manager.WelcomeSentence> wsList = welcomeSentenceEnquiryDao
				.getWelcomeSentenceByLanguage(langaugeId);
		if (null != wsList) {
			return Lists.transform(
					wsList,
					obj -> {
						return new WelcomeSentence(obj.getLanguage(), obj
								.getWelcomeSentence());
					});
		}
		return null;
	}

}
