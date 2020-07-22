package com.idowran.rabbitmq.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author : ryan
 * @CreateTime : 2020-07-21
 * @Description :
 **/
@RestController
public class SenderController {
 
	private String MESSAGE_ID 	= String.valueOf(UUID.randomUUID());
	private String CREATE_TIME 	= LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	
    @Autowired
	private RabbitTemplate rabbitTemplate;  // 使用RabbitTemplate, 这提供了接收/发送等等方法


    @GetMapping("/sendFanoutMessage")
    public String sendFanoutMessage() {
    	// 扇形(发布/订阅)
        String messageData 	= "message: testFanoutMessage";
        
        Map<String, String> map = new HashMap<>();
        map.put("messageId", MESSAGE_ID);
        map.put("createTime", CREATE_TIME);
        map.put("messageData", messageData);
        
        // 发送消息到交换机fanoutExchange上, 不指定路由key(指定也没用)
        rabbitTemplate.convertAndSend("fanoutExchange", null, map);
        return "ok";
    }
    
    @GetMapping("/sendDirectMessage")
    public String sendDirectMessage(){
    	// 路由
        String messageData  = "message: testDirectMessage!";
        
        Map<String, String> map = new HashMap<>();
        map.put("messageId", MESSAGE_ID);
        map.put("createTime", CREATE_TIME);
        map.put("messageData", messageData);
        
        // 将消息携带绑定键值：发送到交换机testDirectExchange上, 并携带路由key testDirectRouting 
        rabbitTemplate.convertAndSend("directExchange", "directRouting", map);
        return "ok";
    }
    
    @GetMapping("/sendTopicMessage1")
    public String sendTopicMessage1() {
    	// 主题1
        String messageData 	= "message: M A N";
        
        Map<String, String> manMap = new HashMap<>();
        manMap.put("messageId", MESSAGE_ID);
        manMap.put("createTime", CREATE_TIME);
        manMap.put("messageData", messageData);
        
        // 将消息发送到交换机topicExchange上, 并携带路由keytopic.man
        rabbitTemplate.convertAndSend("topicExchange", "topic.man", manMap);
        return "ok";
    }
 
    @GetMapping("/sendTopicMessage2")
    public String sendTopicMessage2() {
    	// 主题2
        String messageData 	= "message: woman is all";
        
        Map<String, String> womanMap = new HashMap<>();
        womanMap.put("messageId", MESSAGE_ID);
        womanMap.put("createTime", CREATE_TIME);
        womanMap.put("messageData", messageData);
        
        // 将消息发送到交换机topicExchange上, 并携带路由keytopic.woman
        rabbitTemplate.convertAndSend("topicExchange", "topic.woman", womanMap);
        return "ok";
    }
    
}