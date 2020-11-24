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
        logger.info("\n [DELIVERY_TAG]->[{}]\n [CONSUMER_TAG]->[{}]\n [RECEIVED_EXCHANGE]->[{}]\n [RECEIVED_ROUTING_KEY]->[{}]",
                tag, properties.getConsumerTag(), properties.getReceivedExchange(), properties.getReceivedRoutingKey());
        logger.warn("receive message is [{}]", msg);
        channel.basicAck(tag, true);
    }
}
