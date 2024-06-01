package org.dromara.common.websocket.holder;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocketSession 用于保存当前所有在线的会话信息
 *
 * @author zendwang
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WebSocketSessionHolder {

    /**
     * 用户会话映射，用于存储用户ID与WebSocket会话的映射关系。
     */
    private static final Map<Long, WebSocketSession> USER_SESSION_MAP = new ConcurrentHashMap<>();

    /**
     * 添加一个用户的WebSocket会话。
     * @param sessionKey 用户的唯一标识。
     * @param session 用户对应的WebSocket会话实例。
     */
    public static void addSession(Long sessionKey, WebSocketSession session) {
        USER_SESSION_MAP.put(sessionKey, session);
    }

    /**
     * 移除一个用户的WebSocket会话。
     * @param sessionKey 用户的唯一标识。
     */
    public static void removeSession(Long sessionKey) {
        if (USER_SESSION_MAP.containsKey(sessionKey)) {
            USER_SESSION_MAP.remove(sessionKey);
        }
    }

    /**
     * 根据用户ID获取对应的WebSocket会话。
     * @param sessionKey 用户的唯一标识。
     * @return 返回对应的WebSocket会话，如果不存在，则返回null。
     */
    public static WebSocketSession getSessions(Long sessionKey) {
        return USER_SESSION_MAP.get(sessionKey);
    }

    /**
     * 获取所有用户的ID集合。
     * @return 返回所有用户ID的集合。
     */
    public static Set<Long> getSessionsAll() {
        return USER_SESSION_MAP.keySet();
    }

    /**
     * 检查一个用户ID是否存在对应的WebSocket会话。
     * @param sessionKey 用户的唯一标识。
     * @return 如果存在，则返回true，否则返回false。
     */
    public static Boolean existSession(Long sessionKey) {
        return USER_SESSION_MAP.containsKey(sessionKey);
    }

}
