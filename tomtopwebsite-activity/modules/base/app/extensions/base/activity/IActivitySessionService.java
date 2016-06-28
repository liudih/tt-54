package extensions.base.activity;

import java.util.List;
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

public interface IActivitySessionService {

	public <V> RBucket<V> getBucket(String name);

	public <V> List<RBucket<V>> getBuckets(String pattern);

	public <V> RHyperLogLog<V> getHyperLogLog(String name);

	public <V> RList<V> getList(String name);

	public <K, V> RMap<K, V> getMap(String name);

	public RLock getLock(String name);

	public <V> RSet<V> getSet(String name);

	public RScript getScript();

	public <V> RSortedSet<V> getSortedSet(String name);

	public <M> RTopic<M> getTopic(String name);

	public <V> RQueue<V> getQueue(String name);

	public <V> RBlockingQueue<V> getBlockingQueue(String name);

	public <V> RDeque<V> getDeque(String name);

	public RAtomicLong getAtomicLong(String name);

	public RCountDownLatch getCountDownLatch(String name);
}
