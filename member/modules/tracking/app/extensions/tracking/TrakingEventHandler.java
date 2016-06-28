package extensions.tracking;

import java.util.List;

import javax.inject.Inject;

import play.Logger;
import service.tracking.IVisitLogService;

import com.google.common.eventbus.Subscribe;

import entity.tracking.VisitLog;

public class TrakingEventHandler {

	@Inject
	IVisitLogService visitLogService;

	@Subscribe
	public void onVisitPathEvent(VisitLog visitLog) {
		try {
			visitLogService.saveVisitLog(visitLog.getIwebsiteid(),
					visitLog.getCaid(), visitLog.getCsource(),
					visitLog.getCip(), visitLog.getCpath(), visitLog.getCeid(),
					visitLog.getItasktype());
		} catch (Throwable e) {
			Logger.error("save visitpat log Error", e);
		}
	}

}
