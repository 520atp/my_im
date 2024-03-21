package top.mangod.springwebsocket.listener;

import cn.hutool.core.collection.CollUtil;
import com.rabbitmq.client.Channel;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.mangod.springwebsocket.component.MessageStore;
import top.mangod.springwebsocket.domain.RabbitMsg;

import javax.websocket.Session;
import java.io.IOException;
import java.util.List;

import static top.mangod.springwebsocket.constant.ExchangeConstants.FANOUT_QUERY_SESSION_EXCHANGE;
import static top.mangod.springwebsocket.constant.QueueConstants.QUERY_SESSION_QUEUE_A;

@Component
@RabbitListener(bindings = @QueueBinding(
        // value = @Queue(value = "queue1"),
        value = @Queue(), //切记： 此处无需设置队列名称，否在得话，多个消费者只有一个消费者能消费数据。其它消费者无法消费数据。
        exchange = @Exchange(value = FANOUT_QUERY_SESSION_EXCHANGE,type = ExchangeTypes.FANOUT)
))
@Log4j2
public class FanoutReceiver {

    @Autowired
    private MessageStore messageStore;


    /**
     * @param msg		接收到的消息
     * @param message   消息主题
     * @param channel   队列签收
     */
    @RabbitHandler
    public void handle(RabbitMsg msg, Message message, Channel channel) throws IOException {
        log.info("消息队列：[{}]，接收到消息：[{}]",QUERY_SESSION_QUEUE_A, msg);
        List<Session> sessionList = messageStore.getLocalSessionByUserId(msg.getUserId());
        if (CollUtil.isEmpty(sessionList)) {
        } else {
            for(Session toSession:sessionList){
                toSession.getBasicRemote().sendText(msg.getMsg());
            }

        }
    }
}
