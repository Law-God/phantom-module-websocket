package com.phantom.module.websocket;

import com.phantom.util.string.StringUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.nio.ByteBuffer;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zzk
 * @version 1.0
 * @created 25-八月-2017 11:15:02
 */
@ServerEndpoint(value = "/phantom/websocket",configurator = HttpSessionConfigurator.class)
public class PhantomWebSocketServer {
	private Log log = LogFactory.getLog(PhantomWebSocketServer.class);

	private String httpSessionId;
	/**
	 * 在线连接数
	 */
	public static AtomicInteger onlineCount = new AtomicInteger(0);
	/**
	 * 客户连接Sesson
	 */
	private Session session;
	/**
	 * <font color="#808080"><i>线程安全Set，用来存放每个客户端对应的MyWebSocket对象</i></font>
	 */
	private static CopyOnWriteArraySet<PhantomWebSocketServer> webSocketSet = new CopyOnWriteArraySet<PhantomWebSocketServer>();

	public PhantomWebSocketServer(){
		log.debug(this);
	}

	/**
	 * 客户端连接时调用
	 * 
	 * @param session
	 */
	@OnOpen
	public void onOpen(Session session,EndpointConfig config){
		log.debug("phantom websocket open...");
		this.session = session;
		HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
		this.httpSessionId = httpSession.getId();
		webSocketSet.add(this);
		System.out.println(session.getId());
	}

	/**
	 * 客户端发送消息时调用
	 * 
	 * @param msg
	 * @param session
	 */
	@OnMessage
	public void onMessage(String msg, Session session){
		log.debug("phantom websocket onMessage...");
		sendMessage(msg);
	}

	/**
	 * 连接关闭时调用
	 */
	@OnClose
	public void onClose(){
		log.debug("phantom websocket onClose...");
		webSocketSet.remove(this);
	}

	/**
	 * 发生错误时调用
	 * 
	 * @param session
	 * @param e
	 */
	@OnError
	public void onError(Session session, Throwable e){
		log.debug("phantom websocket onError...");
		e.printStackTrace();
	}

	/**
	 * 给指定客户端推送信息
	 * 
	 * @param session
	 * @param msg
	 */
	public void sendMessage(Session session, String msg){
		session.getAsyncRemote().sendText(msg);
	}

	/**
	 * 给所有客户端推送信息
	 */
	public void sendMessage(String msg){
		if(StringUtils.isEmpty(msg)) return;
		for(PhantomWebSocketServer phantomWebSocketServer : webSocketSet){
			//phantomWebSocketServer.getSession().getAsyncRemote().sendText(msg);
			phantomWebSocketServer.getSession().getAsyncRemote().sendText(msg);
		}
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}
}