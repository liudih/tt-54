package services.messaging.impl;

import java.util.HashMap;
import java.util.List;

import play.Logger;
import mapper.messaging.MessageMapper;
import services.base.FoundationService;
import services.base.utils.StringUtils;
import services.messaging.IMessageService;
import session.ISessionService;
import valueobjects.base.LoginContext;
import valueobjects.base.Page;

import com.google.inject.Inject;

import dao.messaging.IMessageDao;

import entity.messaging.Broadcast;
import entity.messaging.MessageInfo;
import enums.messaging.Status;
public class MessageService implements IMessageService {

	@Inject
	private IMessageDao dao;
	@Inject
	private FoundationService fservice;

	@Inject
	private MessageMapper messageMapper;
	
	@Inject
	ISessionService sessionService;
	
	@Override
	public int getMyMessageTotal() {
		// 获取当前用户
		LoginContext lc = fservice.getLoginContext();
		String userId = lc.getMemberID();
		HashMap<String, Object> paras = new HashMap<String, Object>(1);
		paras.put("userId", userId);
		return this.dao.getMyMessageTotal(paras);
	}

	@Override
	public Page<Broadcast> getMyMessageForPage(int page, int pageSize) {
		// 获取当前用户
		LoginContext lc = fservice.getLoginContext();
		String userId = lc.getMemberID();
		HashMap<String, Object> paras = new HashMap<String, Object>(1);
		paras.put("page", page);
		paras.put("pageSize", pageSize);
		paras.put("userId", userId);

		List<Broadcast> messages = this.dao.getMyMessageForPage(paras);
		int total = this.getMyMessageTotal();

		Page<Broadcast> result = new Page<Broadcast>(messages, total, page,
				pageSize);

		return result;
	}

	@Override
	public int readMessage(String id) {
		HashMap<String, Object> paras = new HashMap<String, Object>(2);
		paras.put("id", id);
		paras.put("status", Status.READ.getCode());
		return this.dao.updateMessageStatus(paras);
	}

	@Override
	public int deleteMessage(String id) {
		HashMap<String, Object> paras = new HashMap<String, Object>(2);
		paras.put("id", id);
		paras.put("status", Status.DELETE.getCode());
		return this.dao.updateMessageStatus(paras);
	}

	@Override
	public Broadcast getDetail(String id) {
		HashMap<String, Object> paras = new HashMap<String, Object>(1);
		paras.put("id", id);
		return this.dao.getDetail(paras);
	}

	@Override
	public boolean isExistedByBroadcastId(String broadcastId)
			throws NullPointerException {
		// 获取当前用户
		LoginContext lc = fservice.getLoginContext();
		String userId = lc.getMemberID();
		if (StringUtils.isEmpty(userId)) {
			throw new NullPointerException("can not get member id");
		}

		HashMap<String, Object> paras = new HashMap<String, Object>(2);
		paras.put("userId", userId);
		paras.put("broadcastId", broadcastId);
		return this.dao.isExisted(paras);
	}

	@Override
	public int deleteMessageByBroadcastId(String broadcastId) {
		HashMap<String, Object> paras = new HashMap<String, Object>(2);
		// 获取当前用户
		LoginContext lc = fservice.getLoginContext();
		String userId = lc.getMemberID();

		paras.put("broadcastId", broadcastId);
		paras.put("userId", userId);
		paras.put("status", Status.DELETE.getCode());
		return this.dao.updateMessageStatus(paras);
	}

	@Override
	public int insert(Broadcast paras) {
		return this.dao.insert(paras);
	}
	
	
	public Page<MessageInfo> getAllPersonalMessages(int page,int adminUserId) {	
		
		int pageSize = 10;
		
		int pageIndex = (page - 1) * pageSize;

		List<MessageInfo> list = messageMapper.getAllPersonalMessages(pageIndex,pageSize,adminUserId);

		int total = messageMapper.getCountPersonalMessages(adminUserId);

		return new Page<MessageInfo>(list, total, page, pageSize);
	}
	
	public boolean sendPersonalMessage(Broadcast info) {				
		int flag = 0; 
		flag = messageMapper.insert(info);
		return (flag > 0);
	}
}
