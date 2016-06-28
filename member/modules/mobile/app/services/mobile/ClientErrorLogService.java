package services.mobile;

import java.util.List;

import javax.inject.Inject;

import mapper.ClientErrorLogMapper;
import entity.mobile.MobileClientErrorLog;

public class ClientErrorLogService {

	@Inject
	ClientErrorLogMapper mapper;

	public boolean addClientErrorLog(MobileClientErrorLog log) {
		int result = mapper.insert(log);
		return result > 0 ? true : false;
	}

	public boolean batchAdd(List<MobileClientErrorLog> logs) {
		int result = mapper.batchInsert(logs);
		return result > 0 ? true : false;
	}

}
