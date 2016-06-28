package handlers.research;

import java.util.Date;

import javax.inject.Inject;

import play.Logger;
import com.google.common.eventbus.Subscribe;

import dao.activitydb.page.IVoteRecordDao;
import entity.activity.page.VoteRecord;
import event.research.VoteRecordEvent;

public class VoteRecordEventHandler {

	@Inject
	IVoteRecordDao iVoteRecordDao;

	@Subscribe
	public void onVoteEvent(VoteRecordEvent event) {
		try {
			// ~
			VoteRecord vr = new VoteRecord();
			vr.setCmemberemail(event.getEmail());
			vr.setCvhost(event.getVhost());
			vr.setDvotedate(new Date());
			vr.setIpageitemid(event.getVotePageItemId());
			vr.setIwebsiteid(event.getWebsiteId());
			iVoteRecordDao.insert(vr);
		} catch (Throwable e) {
			Logger.error("Campaign Execution Error", e);
		}
	}

}
