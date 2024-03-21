package top.mangod.springwebsocket.component;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.mangod.springwebsocket.domain.RabbitMsg;

import javax.annotation.PostConstruct;

import static top.mangod.springwebsocket.constant.ExchangeConstants.FANOUT_QUERY_SESSION_EXCHANGE;
import static top.mangod.springwebsocket.constant.ExchangeConstants.TOPIC_QUERY_SESSION_EXCHANGE;
import static top.mangod.springwebsocket.constant.RoutingConstants.ROUTING_SESSION_KEY;


@Component
public class MessageSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init(){
        WebSocketServer.setMessageSender(this);
    }

    public void sendToFanoutExchange(RabbitMsg msg){
        rabbitTemplate.convertAndSend(FANOUT_QUERY_SESSION_EXCHANGE,"",msg);
    }


    public void sendToTopicExchange(RabbitMsg msg){
        rabbitTemplate.convertAndSend(TOPIC_QUERY_SESSION_EXCHANGE,ROUTING_SESSION_KEY,msg);
    }
}
