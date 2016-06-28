package dao.messaging.impl;

import java.util.List;
import java.util.Map;

import mapper.messaging.BroadcastMapper;

import com.google.inject.Inject;

import dao.messaging.IBroadcastDao;
import entity.messaging.Broadcast;

public class BroadcastDao implements IBroadcastDao {

	@Inject
	private BroadcastMapper dbMapper;

	@Override
	public List<Broadcast> selectBroadcasts() {
		return dbMapper.selectBroadcasts();
	}

	@Override
	public List<Broadcast> selectBroadcastsForPage(Map paras) {
		return dbMapper.selectBroadcastsForPage(paras);
	}

	@Override
	public int getTotal(Map paras) {
		return this.dbMapper.getTotal(paras);
	}

	@Override
	public int markMyBroadcastMessage(Map paras) {
		return this.dbMapper.markMyBroadcastMessage(paras);
	}

	@Override
	public Broadcast getDetail(Map paras) {
		return this.dbMapper.getDetail(paras);
	}

	@Override
	public int add(Broadcast m) {
		return this.dbMapper.add(m);
	}

	@Override
	public int update(Broadcast m) {
		return this.dbMapper.update(m);
	}

	@Override
	public int publish(Broadcast m) {
		return this.dbMapper.publish(m);
	}

	@Override
	public int delete(Map paras) {
		return this.dbMapper.delete(paras);
	}
}
