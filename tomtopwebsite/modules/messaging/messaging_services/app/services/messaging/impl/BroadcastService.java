package services.messaging.impl;

import java.util.HashMap;
import java.util.List;

import services.base.FoundationService;
import services.base.utils.StringUtils;
import services.messaging.IBroadcastService;
import valueobjects.base.LoginContext;
import valueobjects.base.Page;

import com.google.inject.Inject;

import dao.messaging.IBroadcastDao;
import entity.messaging.Broadcast;
import enums.messaging.Status;

public class BroadcastService implements IBroadcastService {
	@Inject
	private IBroadcastDao dao;
	@Inject
	private FoundationService fservice;

	public List<Broadcast> selectBroadcasts() {
		return dao.selectBroadcasts();
	}

	@Override
	public Page<Broadcast> selectBroadcastsForPage(int page, int pageSize) {
		// 获取当前用户
		LoginContext lc = fservice.getLoginContext();
		String userId = lc.getMemberID();

		HashMap<String, Object> paras = new HashMap<String, Object>(3);
		paras.put("page", page);
		paras.put("pageSize", pageSize);
		paras.put("userId", userId);

		List<Broadcast> messages = this.dao.selectBroadcastsForPage(paras);

		HashMap<String, Object> totalParas = new HashMap<String, Object>(1);
		totalParas.put("userId", userId);
		int total = this.dao.getTotal(totalParas);

		Page<Broadcast> result = new Page<Broadcast>(messages, total, page,
				pageSize);

		return result;
	}

	@Override
	public int deleteMyBroadcastMessage(int broadcastId) {
		// 获取当前用户
		LoginContext lc = fservice.getLoginContext();
		String userId = lc.getMemberID();
		// 如果当前用户id为空，则操作失败
		if (StringUtils.isEmpty(userId)) {
			return 0;
		}
		int status = Status.DELETE.getCode();

		HashMap<String, Object> paras = new HashMap<String, Object>();
		paras.put("broadcastId", broadcastId);
		paras.put("userId", userId);
		paras.put("status", status);
		return this.dao.markMyBroadcastMessage(paras);
	}

	@Override
	public int readMyBroadcastMessage(String broadcastId) {
		// 获取当前用户
		LoginContext lc = fservice.getLoginContext();
		String userId = lc.getMemberID();
		// 如果当前用户id为空，则操作失败
		if (StringUtils.isEmpty(userId)) {
			return 0;
		}
		int status = Status.READ.getCode();

		HashMap<String, Object> paras = new HashMap<String, Object>();
		paras.put("broadcastId", broadcastId);
		paras.put("userId", userId);
		paras.put("status", status);
		return this.dao.markMyBroadcastMessage(paras);
	}

	@Override
	public Broadcast getDetail(String broadcastId) {
		HashMap<String, Object> paras = new HashMap<String, Object>(1);
		paras.put("id", broadcastId);
		return this.dao.getDetail(paras);
	}

	@Override
	public int add(Broadcast m) {
		return this.dao.add(m);
	}

	@Override
	public int update(Broadcast m) {
		return this.dao.update(m);
	}

	@Override
	public int publish(Broadcast m) {
		return this.dao.publish(m);
	}

	@Override
	public int delete(int id) {
		HashMap<String, Object> paras = new HashMap<String, Object>(1);
		paras.put("id", id);
		return this.dao.delete(paras);
	}
}
