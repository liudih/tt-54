package extensions.manager;

import java.util.List;

import javax.inject.Inject;

import com.google.common.collect.Lists;

import dao.manager.IProfessionSkillTopicEnquiryDao;
import entity.manager.ProfessionSkillTopic;
import extensions.livechat.topic.ChatTopic;
import extensions.livechat.topic.ChatTopicProvider;

public class TTChatTopicProvider implements ChatTopicProvider {

	@Inject
	IProfessionSkillTopicEnquiryDao iProfessionSkillTopicEnquiryDao;

	@Override
	public List<ChatTopic> getTopics(int languageId) {
		// languageId
		List<ProfessionSkillTopic> pstlist = iProfessionSkillTopicEnquiryDao
				.getEnableTopicsByLanguage(languageId);
		if (null != pstlist) {
			return Lists.transform(
					pstlist,
					obj -> {
						return new ChatTopic(String.valueOf(obj.getIid()), obj
								.getCtitle());
					});
		}
		return null;
	}
}
