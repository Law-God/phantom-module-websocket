package com.phantom.module.websocket;


import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017-08-25.
 * WebSocket获取HttpSession工具类
 */
public class HttpSessionConfigurator extends ServerEndpointConfig.Configurator {
    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        super.modifyHandshake(sec, request, response);

        HttpSession httpSession = (HttpSession)request.getHttpSession();

        sec.getUserProperties().put(HttpSession.class.getName(),httpSession);
    }
}
