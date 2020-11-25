package com.example.testconsumer.mq;

import com.example.testconsumer.config.RabbitmqConfig;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author Administrator
 * @version 1.0
 **/
@Component
public class ReceiveHandler {

    Logger logger = LoggerFactory.getLogger(ReceiveHandler.class);

    @RabbitListener(queues = {RabbitmqConfig.QUEUE_INFORM_EMAIL})
    public void send_email(String msg, Message message, Channel channel, @Header(name = AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        MessageProperties properties = message.getMessageProperties();
        logger.info("[DELIVERY_TAG]->[{}]", tag);
        logger.info("[CONSUMER_TAG]->[{}]", properties.getConsumerTag());
        logger.info("[RECEIVED_EXCHANGE]->[{}]", properties.getReceivedExchange());
        logger.info("[RECEIVED_ROUTING_KEY]->[{}]", properties.getReceivedRoutingKey());
        logger.warn("receive message is [{}]", msg);
        //当该参数为 true 时，则可以一次性确认 delivery_tag 小于等于传入值的所有消息
        channel.basicAck(tag, false);
    }
}
