package services.livechat;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.redisson.Redisson;
import org.redisson.core.RBlockingQueue;
import org.redisson.core.RBucket;
import org.redisson.core.RMap;
import org.redisson.core.RQueue;

import play.Logger;
import play.libs.F.Promise;
import valueobjects.livechat.Status;
import valueobjects.livechat.control.Control;
import valueobjects.livechat.control.ControlCodec;
import valueobjects.livechat.data.LiveChatMessage;
import valueobjects.livechat.session.ChatSession;
import valueobjects.livechat.session.ConnectRequest;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.UnmodifiableIterator;

public class RedisChatSessionService implements IChatSessionService {

	final Redisson redisson;

	public RedisChatSessionService(Redisson redisson) {
		this.redisson = redisson;
	}

	@Override
	public void enqueue(LiveChatMessage message) {
		RQueue<LiveChatMessage> q = redisson.getQueue(composeQueueID(
				message.getChatSessionID(), message.getTo()));
		q.add(message);
	}

	@Override
	public List<LiveChatMessage> dequeue(String chatSessionID, String to) {
		RQueue<LiveChatMessage> q = redisson.getQueue(composeQueueID(
				chatSessionID, to));
		List<LiveChatMessage> lists = Lists.newArrayList();
		while (true) {
			LiveChatMessage message = q.poll();
			if (message == null) {
				break;
			}
			lists.add(message);
		}
		return lists;
	}

	@Override
	public void createSession(ChatSession session) {
		RMap<String, ChatSession> map = redisson.getMap("ChatSession");
		map.put(session.getId(), session);
	}

	@Override
	public ChatSession getChatSession(String chatSessionID) {
		RMap<String, ChatSession> map = redisson.getMap("ChatSession");
		return map.get(chatSessionID);
	}

	@Override
	public void clearSession(String chatSessionID) {
		RMap<String, ChatSession> map = redisson.getMap("ChatSession");
		map.remove(chatSessionID);
	}

	@Override
	public List<Control> dequeueControl(String ltc) {
		RBlockingQueue<JsonNode> q = redisson
				.getBlockingQueue(composeControlQueueID(ltc));
		List<Control> controls = Lists.newArrayList();
		while (true) {
			JsonNode node = q.poll();
			if (node == null) {
				break;
			}
			Control c = ControlCodec.decode(node);
			controls.add(c);
		}
		return controls;
	}

	@Override
	public void enqueueControl(String ltc, Control control) {
		RBlockingQueue<JsonNode> q = redisson
				.getBlockingQueue(composeControlQueueID(ltc));
		q.add(ControlCodec.encode(control));
	}

	@Override
	public void setRingStatus(String originLTC, String originAlias,
			String destinationLTC) {
		RBucket<String> q = redisson.getBucket(composeRingBucket(
				destinationLTC, originAlias));
		q.expire(5, TimeUnit.MINUTES);
		q.set(originLTC);
	}

	@Override
	public String getRingStatus(String originAlias, String destinationLTC) {
		RBucket<String> q = redisson.getBucket(composeRingBucket(
				destinationLTC, originAlias));
		return q.get();
	}

	@Override
	public void clearRingStatus(String originAlias, String destinationLTC) {
		RBucket<String> q = redisson.getBucket(composeRingBucket(
				destinationLTC, originAlias));
		q.delete();
	}

	@Override
	public Status getStatus(String ltc) {
		RBucket<Status> bucket = redisson
				.getBucket(composeStatusBucketName(ltc));
		return bucket.get();
	}

	@Override
	public void updateStatus(String ltc, String alias) {
		// LTC index
		RBucket<Status> bucket = redisson
				.getBucket(composeStatusBucketName(ltc));
		bucket.expire(30, TimeUnit.SECONDS);
		Status s = bucket.get();
		if (s == null) {
			s = new Status();
			s.setLtc(ltc);
			s.setLoginAt(null);
		}
		s.setAlias(alias);
		s.setLastSeen(new Date());
		bucket.set(s);

		// Alias index
		RBucket<Status> aliasStatus = redisson
				.getBucket(composeAliasStatusBucketName(alias));
		aliasStatus.expire(30, TimeUnit.SECONDS);
		aliasStatus.set(s);
	}

	@Override
	public void updateStatus(String ltc, Integer ilanguageid, String topic,
			String ip, String country, Integer iroleid) {
		RBucket<Status> bucket = redisson
				.getBucket(composeStatusBucketName(ltc));
		bucket.expire(30, TimeUnit.SECONDS);
		Status s = bucket.get();
		if (s == null) {
			s = new Status();
			s.setLtc(ltc);
			s.setLoginAt(null);
		}
		s.setLastSeen(new Date());
		s.setIp(ip);
		s.setCountry(country);
		s.setIroleid(iroleid);
		s.setIlanguageid(ilanguageid);
		topic = topic.substring(6);
		s.setTopic(topic);
		bucket.set(s);

		// Alias index
		RBucket<Status> aliasStatus = redisson
				.getBucket(composeStatusBucketName(ltc));
		aliasStatus.expire(30, TimeUnit.SECONDS);
		aliasStatus.set(s);
	}

	@Override
	public Status getStatusByAlias(String alias) {
		// Alias index
		RBucket<Status> aliasStatus = redisson
				.getBucket(composeAliasStatusBucketName(alias));
		return aliasStatus.get();
	}

	@Override
	public void login(String ltc, String alias) {
		// LTC index
		RBucket<Status> bucket = redisson
				.getBucket(composeStatusBucketName(ltc));
		bucket.expire(30, TimeUnit.SECONDS);
		Status s = new Status();
		s.setLtc(ltc);
		s.setLoginAt(new Date());
		s.setLastSeen(new Date());
		s.setAlias(alias);
		bucket.set(s);

		// Alias index
		RBucket<Status> aliasStatus = redisson
				.getBucket(composeAliasStatusBucketName(alias));
		aliasStatus.expire(30, TimeUnit.SECONDS);
		aliasStatus.set(s);
	}

	@Override
	public Collection<ChatSession> getChatSessionsByLTC(String ltc) {
		RMap<String, ChatSession> map = redisson.getMap("ChatSession");
		return Maps.filterValues(
				map,
				s -> ltc.equals(s.getDestination())
						|| ltc.equals(s.getOrigin())).values();
	}

	@Override
	public Promise<Control> waitForControl(String ltc,
			Class<? extends Control> clazz) {
		RBlockingQueue<JsonNode> q = redisson
				.getBlockingQueue(composeControlQueueID(ltc));
		return Promise.promise(() -> {
			while (true) {
				JsonNode n = q.take();
				Logger.debug("Control Polled: {}", n);
				Control c = ControlCodec.decode(n);
				if (clazz.isInstance(c)) {
					return c;
				}
			}
		});
	}

	@Override
	public void flushControl(String ltc) {
		redisson.getBlockingQueue(composeControlQueueID(ltc)).clear();
	}

	@Override
	public void enqueueConnectRequest(ConnectRequest connectRequest) {
		RQueue<ConnectRequest> waiting = redisson.getQueue("waiting-queue");
		waiting.add(connectRequest);
	}

	@Override
	public boolean hasRepeatConnectRequest(ConnectRequest connectRequest) {
		RQueue<ConnectRequest> waiting = redisson.getQueue("waiting-queue");
		UnmodifiableIterator<ConnectRequest> list = Iterators.filter(waiting
				.iterator(), cobj -> ((ConnectRequest) cobj).getOriginLTC()
				.equals(connectRequest.getOriginLTC()));
		if (list != null && list.hasNext()) {
			return true;
		}
		return false;
	}

	@Override
	public ConnectRequest peekConnectRequest() {
		RQueue<ConnectRequest> waiting = redisson.getQueue("waiting-queue");
		return waiting.peek();
	}

	@Override
	public void removeConnectRequest(ConnectRequest request) {
		RQueue<ConnectRequest> waiting = redisson.getQueue("waiting-queue");
		waiting.remove(request);
	}

	@Override
	public <X> List<X> mapConnectRequest(Function<ConnectRequest, X> func) {
		RQueue<ConnectRequest> waiting = redisson.getQueue("waiting-queue");
		return FluentIterable.from(waiting).transform(func).toList();
	}

	@Override
	public void removeRelatedConnectRequest(String ltc, String alias) {
		RQueue<ConnectRequest> waiting = redisson.getQueue("waiting-queue");
		waiting.removeIf(cr -> {
			if (alias != null) {
				return ltc.equals(cr.getOriginLTC())
						&& alias.equals(cr.getDestinationAlias());
			} else {
				return ltc.equals(cr.getOriginLTC());
			}
		});
	}

	@Override
	public void deleteStatus(String ltc) {
		RBucket<Status> bucket = redisson
				.getBucket(composeStatusBucketName(ltc));
		Status st = bucket.get();
		if (null != st) {
			Logger.debug("delete status ltc {}  alias {} ", st.getLtc(),
					st.getAlias());
			RBucket<Status> bucket1 = redisson
					.getBucket(composeAliasStatusBucketName(st.getAlias()));
			bucket1.delete();
		}
		bucket.delete();
	}

	@Override
	public Collection<ChatSession> getAllSession() {
		RMap<String, ChatSession> map = redisson.getMap("ChatSession");
		return map.values();
	}

	private String composeRingBucket(String destLTC, String originAlias) {
		return "ring:" + originAlias + "->" + destLTC;
	}

	private String composeStatusBucketName(String ltc) {
		return "status:" + ltc;
	}

	private String composeAliasStatusBucketName(String alias) {
		return "alias-status:" + alias;
	}

	private String composeQueueID(String chatSessionID, String to) {
		return chatSessionID + "-" + to;
	}

	private String composeControlQueueID(String ltc) {
		return "control:" + ltc;
	}

	@Override
	public int getWaitCount(String ltc) {
		RQueue<ConnectRequest> waiting = redisson.getQueue("waiting-queue");
		List<ConnectRequest> crList = Lists.newLinkedList(waiting);
		Collection<ConnectRequest> coll = Collections2.filter(crList,
				cr -> ltc.equals(cr.getOriginLTC()));
		List<ConnectRequest> collList = Lists.newArrayList(coll);

		if (collList == null || collList.size() == 0) {
			return waiting.size();
		}

		return crList.indexOf(collList.get(0)) + 1;
	}
}
