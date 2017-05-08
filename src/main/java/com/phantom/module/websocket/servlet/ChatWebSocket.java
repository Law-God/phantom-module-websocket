package com.phantom.module.websocket.servlet;

import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.WsOutbound;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;

/**
 * @Author 张志凯 https://github.com/Law-God/phantom-module-websocket
 * phantom-module-websocket
 * com.phantom.module.websocket.servlet.ChatWebSocket
 * 2016-12-13 10:34
 */
public class ChatWebSocket extends MessageInbound {

    @Override
    protected void onBinaryMessage(ByteBuffer byteBuffer) throws IOException {
        System.out.println("byteBuffer" + byteBuffer);
    }

    @Override
    protected void onTextMessage(CharBuffer charBuffer) throws IOException {
        System.out.println("charBuffer " + charBuffer);
    }

    @Override
    protected void onClose(int status) {
        super.onClose(status);
        System.out.println("onclose");
    }

    @Override
    protected void onOpen(WsOutbound outbound) {
        super.onOpen(outbound);
        System.out.println("onOpen");
    }
}
