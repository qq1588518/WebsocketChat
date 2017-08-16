package personal.mario.dao.impl;

import javax.websocket.Session;
import org.springframework.stereotype.Repository;
import personal.mario.bean.CopyOnWriteMap;
import personal.mario.dao.ChatSessionStorageDao;

@Repository("chatSessionStorageDao")
public class ChatSessionStorageDaoImpl implements ChatSessionStorageDao {
	private static CopyOnWriteMap<String, CopyOnWriteMap<String, Session>> map = new CopyOnWriteMap<>();
	
	@Override
	public void save(String from, String to, String name, Session session) {
		String storageName = from + to;
		
		CopyOnWriteMap<String, Session> unameSession = map.get(storageName);
		
		if (unameSession == null) {
			storageName = to + from;
			unameSession = map.get(storageName);
		}
		
		if (unameSession == null) {
			unameSession = new CopyOnWriteMap<>();
		}
		
		unameSession.put(name, session);
		map.put(storageName, unameSession);
	}
	
	public Session getSession(String from, String to, String uname) {
		String storageName = from + to;
		
		CopyOnWriteMap<String, Session> unameSession = map.get(storageName);
		
		if (unameSession == null) {
			storageName = to + from;
			unameSession = map.get(storageName);
		}
		
		return unameSession == null ? null : unameSession.get(uname);
	}
}
