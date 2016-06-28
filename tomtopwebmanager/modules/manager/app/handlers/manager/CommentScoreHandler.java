package handlers.manager;

import java.util.Date;

import javax.inject.Inject;

import com.google.common.eventbus.Subscribe;

import valueobjects.livechat.score.LiveChatSessionScore;
import dao.manager.impl.CustomerServiceScoreUpdateDao;
import entity.manager.CustomerServiceScore;

public class CommentScoreHandler {

	@Inject
	CustomerServiceScoreUpdateDao customerServiceScoreUpdateDao;

	@Subscribe
	public int save(LiveChatSessionScore score) {
		CustomerServiceScore cs = new CustomerServiceScore();
		cs.setCcontent(score.getComment());
		cs.setCcustomeralias(score.getCustomerAlias());
		cs.setCcustomerltc(score.getCustomerLtc());
		cs.setCcustomerservicealias(score.getCustomerServiceAlias());
		cs.setCcustomerserviceltc(score.getCustomerServiceLtc());
		cs.setCsessionid(score.getSessionId());
		cs.setCtopic(score.getTopic());
		cs.setDcreatedate(new Date());
		int rows = 0;
		for (Integer typeid : score.getQuestionScore().keySet()) {
			cs.setIscore(score.getQuestionScore().get(typeid));
			cs.setItype(typeid);
			rows += customerServiceScoreUpdateDao.save(cs);
		}
		return rows;
	}
}
