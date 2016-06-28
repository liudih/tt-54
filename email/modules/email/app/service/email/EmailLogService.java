package service.email;

import com.google.inject.Inject;

import entity.email.EmailLog;
import mapper.email.EmailLogMapper;

public class EmailLogService {
	@Inject
	EmailLogMapper emailLogMapper;

	public boolean add(EmailLog emailLog) {
		int result = emailLogMapper.insert(emailLog);
		return result > 1 ? true : false;
	}
	
	public int deleteEmailLogRegular(){
		return emailLogMapper.deleteRegular();
	}
}
