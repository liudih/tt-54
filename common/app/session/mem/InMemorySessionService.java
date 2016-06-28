package session.mem;

import java.io.Serializable;
import java.util.Map;

import session.SessionServiceSupport;

import com.google.common.collect.Maps;

public class InMemorySessionService extends SessionServiceSupport {

	Map<String, Map<String, Serializable>> sessionMaps = Maps
			.newConcurrentMap();

	protected Map<String, Serializable> ensureSessionMapExists(String sessionID) {
		if (!sessionMaps.containsKey(sessionID)) {
			sessionMaps.put(sessionID, Maps.newHashMap());
		}
		return sessionMaps.get(sessionID);
	}

	@Override
	protected Serializable doGet(String name, String sessionID) {
		return ensureSessionMapExists(sessionID).get(name);
	}

	@Override
	protected void doSet(String name, Serializable obj, String sessionID) {
		ensureSessionMapExists(sessionID).put(name, obj);
	}

	@Override
	protected void doRemove(String name, String sessionID) {
		ensureSessionMapExists(sessionID).remove(name);
	}

	@Override
	protected void doDestroy(String sessionID) {
		sessionMaps.remove(sessionID);
	}

}
