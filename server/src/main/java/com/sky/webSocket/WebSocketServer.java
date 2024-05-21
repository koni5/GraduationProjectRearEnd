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
@ServerEndpoint("/ws/{id}")
public class WebSocketServer {

    //存放会话对象
    private static Map<Long, Session> sessionMap = new HashMap();

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("id") Long id) {
        log.info("{}号客户端建立连接----------------------", id);
        sessionMap.put(id, session);
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, @PathParam("id") Long id) {
        System.out.println("收到来自客户端：" + id + "的信息:" + message);
    }

    /**
     * 连接关闭调用的方法
     *
     * @param id
     */
    @OnClose
    public void onClose(@PathParam("id") Long id) {
        log.info("{}客户端断开连接----------------------", id);
        sessionMap.remove(id);
    }

    /**
     * 单独发给店铺
     *
     * @param message
     * @param shopId
     */
    public void sendToShop(String message, Long shopId) {
        Session session = sessionMap.get(shopId);
        if (session != null) {
            try {
                //服务器向店铺后台发送消息
                session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
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
        if (session != null) {
            try {
                //服务器向用户前台发送信息
                session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}
