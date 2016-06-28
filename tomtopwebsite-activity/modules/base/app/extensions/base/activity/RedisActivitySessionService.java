package extensions.base.activity;

import java.util.List;

import org.redisson.Redisson;
import org.redisson.core.RAtomicLong;
import org.redisson.core.RBlockingQueue;
import org.redisson.core.RBucket;
import org.redisson.core.RCountDownLatch;
import org.redisson.core.RDeque;
import org.redisson.core.RHyperLogLog;
import org.redisson.core.RList;
import org.redisson.core.RLock;
import org.redisson.core.RMap;
import org.redisson.core.RQueue;
import org.redisson.core.RScript;
import org.redisson.core.RSet;
import org.redisson.core.RSortedSet;
import org.redisson.core.RTopic;

public class RedisActivitySessionService implements IActivitySessionService {

	Redisson redisson;

	public RedisActivitySessionService(Redisson redisson) {
		this.redisson = redisson;
	}

	@Override
	public <V> RBucket<V> getBucket(String name) {
		return redisson.getBucket(name);
	}

	@Override
	public <V> List<RBucket<V>> getBuckets(String pattern) {
		return redisson.getBuckets(pattern);
	}

	@Override
	public <V> RHyperLogLog<V> getHyperLogLog(String name) {
		return redisson.getHyperLogLog(name);
	}

	@Override
	public <V> RList<V> getList(String name) {
		return redisson.getList(name);
	}

	@Override
	public <K, V> RMap<K, V> getMap(String name) {
		return redisson.getMap(name);
	}

	@Override
	public RLock getLock(String name) {
		return redisson.getLock(name);
	}

	@Override
	public <V> RSet<V> getSet(String name) {
		return redisson.getSet(name);
	}

	@Override
	public RScript getScript() {
		return redisson.getScript();
	}

	@Override
	public <V> RSortedSet<V> getSortedSet(String name) {
		return redisson.getSortedSet(name);
	}

	@Override
	public <M> RTopic<M> getTopic(String name) {
		return redisson.getTopic(name);
	}

	@Override
	public <V> RQueue<V> getQueue(String name) {
		return redisson.getQueue(name);
	}

	@Override
	public <V> RBlockingQueue<V> getBlockingQueue(String name) {
		return redisson.getBlockingQueue(name);
	}

	@Override
	public <V> RDeque<V> getDeque(String name) {
		return redisson.getDeque(name);
	}

	@Override
	public RAtomicLong getAtomicLong(String name) {
		return redisson.getAtomicLong(name);
	}

	@Override
	public RCountDownLatch getCountDownLatch(String name) {
		return redisson.getCountDownLatch(name);
	}

}
