package com.sky.webSocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.HashMap;
import java.util.Map;

/**
 * WebSocket服务
 */
@Component
@Slf4j
@ServerEndpoint("/ws/{sid}")
public class WebSocketServer {

    //存放会话对象
    private static Map<Long, Session> sessionMap = new HashMap();

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("sid") Long sid) {
        log.info("{}号客户端建立连接----------------------", sid);
        sessionMap.put(sid, session);
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, @PathParam("sid") Long sid) {
        System.out.println("收到来自客户端：" + sid + "的信息:" + message);
    }

    /**
     * 连接关闭调用的方法
     *
     * @param sid
     */
    @OnClose
    public void onClose(@PathParam("sid") Long sid) {
        log.info("{}客户端断开连接----------------------", sid);
        sessionMap.remove(sid);
    }

    /**
     * 单独发给店铺
     *
     * @param message
     * @param shopId
     */
    public void sendToShop(String message, Long shopId) {
        Session session = sessionMap.get(shopId);
        try {
            //服务器向店铺后台发送消息
            session.getBasicRemote().sendText(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 单独发送给用户
     *
     * @param message
     * @param orderId
     */
    public void sendToUser(String message, Long orderId) {
        Session session = sessionMap.get(orderId);
        try {
            //服务器向用户前台发送信息
            session.getBasicRemote().sendText(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
