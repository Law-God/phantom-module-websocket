package com.phantom.module.websocket.servlet;

import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author 张志凯 https://github.com/Law-God/phantom-module-websocket
 * phantom-module-websocket
 * com.phantom.module.websocket.servlet.SocketServer
 * 2016-12-13 10:04
 */
public class SocketServer extends WebSocketServlet {
    private final AtomicInteger atomicInteger = new AtomicInteger(0);
    @Override
    protected StreamInbound createWebSocketInbound(String s, HttpServletRequest httpServletRequest) {
        return new ChatWebSocket();
    }
}
