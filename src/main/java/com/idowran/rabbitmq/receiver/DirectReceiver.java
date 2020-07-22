package com.idowran.rabbitmq.receiver;

import java.util.Map;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "testDirectQueue")//监听的队列名称 TestDirectQueue
public class DirectReceiver {
	
	@RabbitHandler
    public void process(Map<String, String> testMessage) {
        System.out.println("DirectReceiver消费者收到消息  : " + testMessage.toString());
    }
}
