package extensions.manager;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import com.google.common.collect.FluentIterable;
import play.Logger;
import play.libs.Json;
import dao.manager.ICustomerServiceScoreTypeEnquiryDao;
import valueobjects.livechat.score.LiveChatScoreQuestion;
import entity.manager.CustomerServiceScoreType;
import extensions.livechat.score.SessionScoreQuestionProvider;

public class CommentScoreQuestionProvider implements
		SessionScoreQuestionProvider {

	@Inject
	ICustomerServiceScoreTypeEnquiryDao iCustomerServiceScoreTypeEnquiryDao;

	@Override
	public List<LiveChatScoreQuestion> getScoreQuestion(int languageid) {
		List<CustomerServiceScoreType> types = iCustomerServiceScoreTypeEnquiryDao
				.getTypeByLanguageId(languageid);
		if (null == types && types.size() == 0)
			return null;

		List<LiveChatScoreQuestion> questionlists = FluentIterable.from(types)
				.transform(obj -> {
					LiveChatScoreQuestion lcq = new LiveChatScoreQuestion();
					lcq.setPriority(obj.getIpriority());
					lcq.setId(obj.getIid());
					lcq.setQuetions(obj.getCdescription());
					return lcq;
				}).toSortedList((o1, o2) -> {
					return Integer.compare(o1.getPriority(), o2.getPriority());
				});
		return questionlists;
	}
}
