package services.mobile;

import javax.inject.Inject;

import mapper.VisitLogMapper;
import entity.mobile.MobileVisitLog;

public class VisitLogService {

	@Inject
	VisitLogMapper mapper;

	public boolean addLog(MobileVisitLog log) {
		int result = mapper.insert(log);
		return result > 0 ? true : false;

	}
}
