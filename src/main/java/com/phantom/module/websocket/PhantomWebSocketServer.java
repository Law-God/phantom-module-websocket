package com.phantom.module.websocket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;
import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
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
	 * 心跳包
	 */
	private Heart heart = new Heart();

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
		heart.start();
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
		log.debug(session.isOpen());
		if(session.isOpen()){
			heart.reset();
			if("心跳包".equals(msg)){
				sendMessage("心跳包");
			}else{
				sendMessage(msg);
			}
		}else{
			webSocketSet.remove(this);
		}
	}

	/**
	 * 连接关闭时调用
	 */
	@OnClose
	public void onClose(){
		log.debug("phantom websocket onClose...");
		try {
			webSocketSet.remove(this);
			session.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

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
		try {
			webSocketSet.remove(this);
			session.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
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
			phantomWebSocketServer.getSession().getAsyncRemote().sendText(msg);
		}
	}

	/**
	 * 心跳包检测
	 */
	class Heart{
		//心跳时间10秒
		private long heartTime = 10000;
		//心跳检测次数
		private int heartCheckCount = 5;

		Timer timer = new Timer();

		public void start(){
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					System.out.println("心跳剩余检测次数："+heartCheckCount+"");
					if(heartCheckCount-- > 0){
						sendMessage(session,"心跳包");
					}else{
						stop();
						onClose();
					}
				}
			},heartTime,heartTime);
		}

		public void stop(){
			timer.cancel();
		}

		public void reset(){
			this.heartCheckCount = 5;
		}


	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}
}