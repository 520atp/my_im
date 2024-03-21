package top.mangod.springwebsocket.component;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import top.mangod.springwebsocket.constant.SocketConstants;
import top.mangod.springwebsocket.domain.MsgBody;
import top.mangod.springwebsocket.domain.RabbitMsg;
import top.mangod.springwebsocket.domain.SocketMsg;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;

/**
 *  WebSocket服务
 */
@ServerEndpoint("/trtc/websocket/{userId}")
@Component
@Log4j2
public class WebSocketServer {

    public static MessageStore messageStore;
    public static MessageSender messageSender;

    public static void setMessageStore(MessageStore messageStore){
        WebSocketServer.messageStore = messageStore;
    }

    public static void setMessageSender(MessageSender messageSender){
        WebSocketServer.messageSender = messageSender;
    }

    @OnOpen
    public  void onOpen(Session session, @PathParam("userId") String userId){
        log.info("当前用户:{}已建立！",userId);
        messageStore.saveSession(session);
        messageStore.saveSessionUser(session,userId);
    }

    @OnError
    public void onError(Session session, @PathParam("userId") String userId,Throwable error){
        log.error("通信报错，userId:{},error:{}",userId,error);
        // todo 推送失败，需要将离线消息落库

    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        log.info("收到消息:{}", message);
        if (StringUtils.isEmpty(message)) {
            return;
        }
        SocketMsg<MsgBody> msgBody = JSON.parseObject(message, new TypeReference<SocketMsg<MsgBody>>() {
        });
        // 登录鉴权
        if (SocketConstants.MsgType.LOGIN == msgBody.getMsgType()) {
            // todo 登录实现

        }
        // 心跳检测
        if (SocketConstants.MsgType.HEART_BEAT == msgBody.getMsgType()) {
            session.getBasicRemote().sendText("pong");
            return;
        }
        List<Session> sessionList = messageStore.getLocalSessionByUserId(msgBody.getReceivedUserId());
        /**
         * 如果当前服务未拿到session，有两种情况
         * 单体应用下，直接落库离线消息
         * 分布式负载下，广播所有负载搜索session
         */
        if (CollUtil.isEmpty(sessionList)){
            RabbitMsg msg = new RabbitMsg();
            msg.setUserId(msgBody.getReceivedUserId());
            msg.setMsg(msgBody.getMsgBody().getMessage());
            messageSender.sendToFanoutExchange(msg);
        } else {
            for (Session toSession : sessionList) {
                toSession.getBasicRemote().sendText(msgBody.getMsgBody().getMessage());
            }
        }
    }

    @OnClose
    public void onClose(Session session, @PathParam("userId") String userId){
        messageStore.removeSession(session);
    }

}
