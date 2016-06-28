package services.livechat;

import java.util.Collection;
import java.util.List;

import play.libs.F.Promise;
import valueobjects.livechat.Status;
import valueobjects.livechat.control.Control;
import valueobjects.livechat.data.LiveChatMessage;
import valueobjects.livechat.session.ChatSession;
import valueobjects.livechat.session.ConnectRequest;

import com.google.common.base.Function;

public interface IChatSessionService {

	public abstract List<LiveChatMessage> dequeue(String chatSessionID,
			String to);

	public abstract void enqueue(LiveChatMessage message);

	public abstract List<Control> dequeueControl(String ltc);

	public abstract void enqueueControl(String ltc, Control control);

	public abstract ChatSession getChatSession(String chatSessionID);

	public abstract void createSession(ChatSession session);

	public abstract void updateStatus(String ltc, String alias);

	public abstract Status getStatus(String ltc);

	public abstract Status getStatusByAlias(String alias);

	public abstract Promise<Control> waitForControl(String ltc,
			Class<? extends Control> clazz);

	public abstract void flushControl(String ltc);

	public abstract String getRingStatus(String originAlias,
			String destinationLTC);

	public abstract void setRingStatus(String originLTC, String originAlias,
			String destinationLTC);

	public abstract void clearRingStatus(String originAlias,
			String destinationLTC);

	public abstract void clearSession(String id);

	public abstract void login(String ltc, String alias);

	public abstract Collection<ChatSession> getChatSessionsByLTC(String ltc);

	public abstract void enqueueConnectRequest(ConnectRequest connectRequest);

	public abstract ConnectRequest peekConnectRequest();

	public abstract void removeConnectRequest(ConnectRequest request);

	public abstract void removeRelatedConnectRequest(String ltc, String alias);

	public abstract <X> List<X> mapConnectRequest(
			Function<ConnectRequest, X> func);

	public abstract Collection<ChatSession> getAllSession();

	public abstract void deleteStatus(String ltc);
	
	public abstract boolean hasRepeatConnectRequest(ConnectRequest connectRequest);

	public abstract void updateStatus(String ltc, Integer ilanguageid, String topic, String ip, String country, Integer iroleid);
	
	public abstract int getWaitCount(String ltc);
}
