package top.mangod.springwebsocket.component;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.websocket.Session;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Log4j2
public class MessageStore {

    // 保存所有用户的session
    public static Map<String, Session> sessionMap = new ConcurrentHashMap<>();
    // 保存所有sessionId和用户的关系
    public static Map<String,String> sessionUserMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void init(){
        WebSocketServer.setMessageStore(this);
    }

    /**
     * 根据 sessionId获取本地Session，因为session无法序列法，不能存入redis
     * @param sessionId
     * @return
     */
    public Session getLocalSessionBySessionId(String sessionId){
        return MessageStore.sessionMap.get(sessionId);
    }

    /**
     * 根据 userId获取本地Session，因为session无法序列法，不能存入redis
     * 考虑到账号对应多设备问题，可能一个账号对应多个session
     * @param userId
     * @return
     */
    public List<Session> getLocalSessionByUserId(String userId){
        List<Session> sessionList = new ArrayList<>();
        for (Map.Entry<String, String> entry : sessionUserMap.entrySet()) {
            if (entry.getValue().equals(userId)) {
                Session session = sessionMap.get(entry.getKey());
                sessionList.add(session);
            }
        }
        return sessionList;
    }

    /**
     * 保存session
     * @param session
     */
    public void saveSession(Session session){
        sessionMap.put(session.getId(),session);
    }

    /**
     * 保存sessionUser
     * @param session
     */
    public void saveSessionUser(Session session,String userId){
        sessionUserMap.put(session.getId(),userId);
    }

    /**
     * 清理session
     */
    public void removeSession(Session session){
        String sessionId = session.getId();
        sessionMap.remove(sessionId);
        sessionUserMap.remove(sessionId);
    }
}
