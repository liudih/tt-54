package dao.messaging.impl;

import java.util.List;
import java.util.Map;

import mapper.messaging.MessageMapper;

import com.google.inject.Inject;

import dao.messaging.IMessageDao;
import entity.messaging.Broadcast;

public class MessageDao implements IMessageDao {

	@Inject
	private MessageMapper dbMapper;

	@Override
	public int getMyMessageTotal(Map paras) {
		return this.dbMapper.getMyMessageTotal(paras);
	}

	@Override
	public List<Broadcast> getMyMessageForPage(Map paras) {
		return this.dbMapper.getMyMessageForPage(paras);
	}

	@Override
	public int updateMessageStatus(Map paras) {
		return this.dbMapper.updateMessageStatus(paras);
	}

	@Override
	public Broadcast getDetail(Map paras) {
		return this.dbMapper.getDetail(paras);
	}

	@Override
	public boolean isExisted(Map paras) {
		int nums = this.dbMapper.isExisted(paras);
		return nums > 0 ? true : false;
	}

	@Override
	public int insert(Broadcast paras) {
		return this.dbMapper.insert(paras);
	}

}
