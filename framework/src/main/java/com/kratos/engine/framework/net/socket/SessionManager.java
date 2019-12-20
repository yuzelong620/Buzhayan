package com.kratos.engine.framework.net.socket;

import com.kratos.engine.framework.net.socket.message.Message;
import io.netty.channel.Channel;
import lombok.extern.log4j.Log4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Log4j
public class SessionManager {
    public static SessionManager instance = new SessionManager();

    public static SessionManager getInstance() {
        return instance;
    }

	/** 缓存通信上下文环境对应的登录用户（主要用于服务） */
	private Map<IoSession, Long> session2Players  = new ConcurrentHashMap<>();

	/** 缓存用户id与对应的会话 */
	private ConcurrentMap<Long, IoSession> player2Sessions = new ConcurrentHashMap<>();

	/**
	 *  向单一在线用户发送数据包
	 */
	public void sendPacketTo(IoSession session, Message pact) {
		if(pact == null || session == null) return;
		session.sendPacket(pact);
	}

	public void sendPacketTo(Long playerId, Message pact) {
		if(pact == null || playerId <= 0) return;

		IoSession session = player2Sessions.get(playerId);
		if (session != null) {
			session.sendPacket(pact);
		}
	}

	/**
	 *  向所有在线用户发送数据包
	 */
	public void sendPacketToAllUsers(Message pact){
		if(pact == null ) return;

		player2Sessions.values().forEach( (session) -> session.sendPacket(pact));
	}

	public IoSession getSessionBy(long playerId) {
		return this.player2Sessions.get(playerId);
	}
	
	public long getPlayerIdBy(IoSession session) {
		return this.session2Players.get(session);
	}

	public boolean registerSession(long playerId, IoSession session) {
	    session.setPlayerId(playerId);
		player2Sessions.put(playerId, session);
		log.info(String.format("[%s] registered...", playerId));
		return true;
	}

	/**
	 *  注销用户通信渠道
	 */
	public void unregisterPlayerChannel(Channel context) {
		if(context == null) {
			return;
		}
		IoSession session = ChannelUtils.getSessionBy(context);
        session2Players.remove(session);
		if (session != null) {
            long playerId = session.getPlayerId();
            player2Sessions.remove(playerId);
			session.close(SessionCloseReason.OVER_TIME);
		}
	}

	public Integer getOnlinePlayerCount() {
		return player2Sessions.size();
	}
}
