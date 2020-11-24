package com.example.testproducer;

import com.example.testproducer.config.RabbitmqConfig;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
            String msg = "send email message to user >>>" + i;

            //消息内容和属性
            MessageProperties messageProperties = new MessageProperties();
            Message message = new Message(msg.getBytes(), messageProperties);

            //给关联数据设置ID与消息，假如给把这个ID设置为数据库主键ID，可以在推送成功后落库更改
            CorrelationData data = new CorrelationData(String.valueOf(i));
            data.setReturnedMessage(message);

            rabbitTemplate.convertAndSend(RabbitmqConfig.EXCHANGE_TOPICS_INFORM, "inform.email", msg, data);
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
        rabbitTemplate.convertAndSend(RabbitmqConfig.EXCHANGE_TOPICS_INFORM, "error", message);
        Thread.sleep(5000);
    }
}
