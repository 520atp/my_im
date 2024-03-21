package top.mangod.springwebsocket.conf;

import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static top.mangod.springwebsocket.constant.ExchangeConstants.FANOUT_QUERY_SESSION_EXCHANGE;
import static top.mangod.springwebsocket.constant.ExchangeConstants.TOPIC_QUERY_SESSION_EXCHANGE;
import static top.mangod.springwebsocket.constant.QueueConstants.QUERY_SESSION_QUEUE_A;
import static top.mangod.springwebsocket.constant.QueueConstants.QUERY_SESSION_QUEUE_B;
import static top.mangod.springwebsocket.constant.RoutingConstants.ROUTING_SESSION_KEY;

@Configuration
@Log4j2
public class RabbitmqFanoutConfig {

    /**
     * 初始化队列A -> 各应用负载搜寻本地session
     * 第二个参数TRUE开启消息签收
     */
    @Bean(name = QUERY_SESSION_QUEUE_A)
    public Queue queueA() {
        Queue queue = new Queue(QUERY_SESSION_QUEUE_A,true);
        log.info("队列：[{}]初始化成功................",queue.getName());
        return queue;
    }

    /**
     * 初始化队列B -> 各应用负载搜寻本地session
     * 第二个参数TRUE开启消息签收
     */
    @Bean(name = QUERY_SESSION_QUEUE_B)
    public Queue queueB() {
        Queue queue = new Queue(QUERY_SESSION_QUEUE_B,true);
        log.info("队列：[{}]初始化成功................",queue.getName());
        return queue;
    }

    /**
     * 初始化fanout交换机 -> 发布订阅功能
     * 第二个参数TRUE开启消息签收
     */
    @Bean
    public FanoutExchange fanoutExchange() {
        FanoutExchange fanoutExchange = new FanoutExchange(FANOUT_QUERY_SESSION_EXCHANGE);
        log.info("交换机：[{}]初始化成功................",fanoutExchange.getName());
        return fanoutExchange;
    }

    /**
     * 初始化topic交换机 -> 订阅主题
     * 第二个参数TRUE开启消息签收
     */
    @Bean
    public TopicExchange topicExchange() {
        //durable(true) 持久化，mq重启之后交换机还在
        TopicExchange topicExchange = new TopicExchange(TOPIC_QUERY_SESSION_EXCHANGE,true,false);
        log.info("交换机：[{}]初始化成功................",topicExchange.getName());
        return topicExchange;
    }

    /**
     * 绑定队列A到fanout交换机
     * 通过@Qualifier注解指定要绑定的队列到交换机
     */
    @Bean
    public Binding fanoutExchangeBindingQueueA(@Qualifier(QUERY_SESSION_QUEUE_A) Queue queue,FanoutExchange fanoutExchange) {
        Binding binding = BindingBuilder.bind(queue).to(fanoutExchange);
        log.info("消息队列：[{}] 成功绑定到交换机：[{}] ................",queue.getName(),fanoutExchange.getName());
        return binding;
    }

    /**
     * 绑定队列B到topic交换机
     * 指定routingKey
     */
    @Bean
    public Binding topicExchangeBindingQueueB(@Qualifier(QUERY_SESSION_QUEUE_B) Queue queue, TopicExchange topicExchange) {
        Binding binding = BindingBuilder.bind(queue).to(topicExchange).with(ROUTING_SESSION_KEY);
        log.info("消息队列：[{}] 成功绑定到交换机：[{}] ................",queue.getName(),topicExchange.getName());
        return binding;
    }


}
