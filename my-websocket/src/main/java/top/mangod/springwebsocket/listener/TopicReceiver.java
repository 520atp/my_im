package top.mangod.springwebsocket.listener;

import cn.hutool.core.collection.CollUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.mangod.springwebsocket.component.MessageStore;
import top.mangod.springwebsocket.domain.RabbitMsg;

import javax.websocket.Session;
import java.io.IOException;
import java.util.List;

import static top.mangod.springwebsocket.constant.ExchangeConstants.TOPIC_QUERY_SESSION_EXCHANGE;
import static top.mangod.springwebsocket.constant.QueueConstants.QUERY_SESSION_QUEUE_A;
import static top.mangod.springwebsocket.constant.QueueConstants.QUERY_SESSION_QUEUE_B;

@Log4j2
@RabbitListener(bindings = @QueueBinding(
        value = @Queue(value = QUERY_SESSION_QUEUE_B,durable = "true",autoDelete = "false"),
        exchange = @Exchange(value = TOPIC_QUERY_SESSION_EXCHANGE,type = ExchangeTypes.TOPIC),
        key = "#.msg.#"
))
@Service
public class TopicReceiver {

    @Autowired
    private MessageStore messageStore;


    @RabbitHandler
    public void hand(RabbitMsg msg) throws IOException {
        log.info("消息队列：[{}]，接收到消息：[{}]",QUERY_SESSION_QUEUE_B, msg);
        List<Session> sessionList = messageStore.getLocalSessionByUserId(msg.getUserId());
        if (CollUtil.isEmpty(sessionList)) {
            /**
             *  获取分布式锁
             *  通过标志位判断是否有其他实例搜寻到session
             *  判断是否为最后一个检测的实例
             *  如果是，计数器归位,且异步落库离线消息
             *  如果不是，计数器扣减
             *  释放分布式锁，在finally中释放
             */
        } else {
            /**
             * 获取分布式锁
             * 找到session，设置标志位，计数器归位
             * 释放分布式锁，在finally中释放
             * 推送ws数据，异步落库消息
             */
            for(Session toSession:sessionList){
                toSession.getBasicRemote().sendText(msg.getMsg());
            }

        }

    }
}
