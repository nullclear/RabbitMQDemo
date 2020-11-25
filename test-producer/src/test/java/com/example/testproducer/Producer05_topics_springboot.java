package com.example.testproducer;

import com.example.testproducer.config.RabbitmqConfig;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

/**
 * @author Administrator
 * @version 1.0
 **/
@SuppressWarnings("DanglingJavadoc")
@SpringBootTest
public class Producer05_topics_springboot {
    @Autowired
    RabbitTemplate rabbitTemplate;

    //使用rabbitTemplate发送消息
    @Test
    public void testSendEmail() throws InterruptedException {
        /**
         * 参数：
         * 1、交换机名称
         * 2、routingKey
         * 3、消息内容
         */
        for (int i = 0; i < 2; i++) {
            String msg = "send email message to user >>> " + i;

            //消息内容和属性
            MessageProperties messageProperties = new MessageProperties();
            messageProperties.setMessageId(UUID.randomUUID().toString());
            messageProperties.setDeliveryMode(MessageDeliveryMode.NON_PERSISTENT);
            messageProperties.setExpiration("20000"); // 设置过期时间，单位：毫秒
            messageProperties.setReceivedExchange(RabbitmqConfig.EXCHANGE_TOPICS_INFORM);
            messageProperties.setReceivedRoutingKey("inform.email");
            Message message = new Message(msg.getBytes(), messageProperties);
            //给关联数据设置ID与消息，假如给把这个ID设置为数据库主键ID，可以在推送成功后落库更改
            CorrelationData data = new CorrelationData(String.valueOf(i));
            data.setReturnedMessage(message);

            rabbitTemplate.convertAndSend(RabbitmqConfig.EXCHANGE_TOPICS_INFORM, "inform.email", message, data);
        }
        Thread.sleep(5000);
    }

    //使用rabbitTemplate发送消息
    @Test
    public void testSendPostPage() throws InterruptedException {
        //Map<String, String> message = new HashMap<>();
        //message.put("pageId", "5a795ac7dd573c04508f3a56");
        ////将消息对象转成json串
        //String messageString = JSON.toJSONString(message);
        ////路由key，就是站点ID
        //String routingKey = "5a751fab6abb5044e0d19ea1";
        ///**
        // * 参数：
        // * 1、交换机名称
        // * 2、routingKey
        // * 3、消息内容
        // */
        //rabbitTemplate.convertAndSend("ex_routing_cms_postpage", routingKey, messageString);

        //错误的路由地址会导致推送失败
        String message = "send email message to user";
        rabbitTemplate.convertAndSend(RabbitmqConfig.EXCHANGE_TOPICS_INFORM, "error", message, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                MessageProperties props = message.getMessageProperties();
                //如果选择持久化，服务器重启后消息还在，非持久化，服务器重启后消息就不在了
                props.setDeliveryMode(MessageDeliveryMode.NON_PERSISTENT);
                //过期时间与消息持久化并无关系，假如设置非持久化，在消息过期前，服务器重启了
                //消息依然会没有，假如设置持久化，在重启后，消息会继续计算什么时候过期
                props.setExpiration("2000");//单位: 毫秒
                return message;
            }
        });
        Thread.sleep(5000);
    }
}
