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
public class SenderCallbackController {
 
	private String MESSAGE_ID 	= String.valueOf(UUID.randomUUID());
	private String CREATE_TIME 	= LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	
    @Autowired
	private RabbitTemplate rabbitTemplate;  // 使用RabbitTemplate, 这提供了接收/发送等等方法

    /*
     * 推送消息存在四种情况:
     * ① 消息推送到server，但是在server里找不到交换机
     * ② 消息推送到server，找到交换机了，但是没找到队列
     * ③ 消息推送到server，交换机和队列啥都没找到
     * ④ 消息推送成功
     */
    /*
     * 结论: 
     * ① 这种情况触发的是 ConfirmCallback 回调函数。
     * ②这种情况触发的是 ConfirmCallback和RetrunCallback两个回调函数。
     * ③ 消息推送到server，交换机和队列啥都没找到, 其实和① 是一样的, 没有找到交换机, 就无从说起队列了, 所以结论也跟①一样
     * ④这种情况触发的是 ConfirmCallback 回调函数。
     */
    
    @GetMapping("/testMessageAck1")
    public String testMessageAck1() {
    	// ①  消息推送到server，但是在server里找不到交换机
    	// ③ 消息推送到server，交换机和队列啥都没找到
    	
        String messageData = "message: non-existent-exchange test message";
        
        Map<String, Object> map = new HashMap<>();
        map.put("messageId", MESSAGE_ID);
        map.put("createTime", CREATE_TIME);
        map.put("messageData", messageData);
        
        // 没有交换机, 也就说不上队列了
        rabbitTemplate.convertAndSend("non-existent-exchange", "testDirectRouting", map);
        return "ok";
    }
    
    @GetMapping("/TestMessageAck2")
    public String TestMessageAck2() {
    	// ② 消息推送到server，找到交换机了，但是没找到队列 
        String messageData = "message: lonelyDirectExchange test message";

        Map<String, Object> map = new HashMap<>();
        map.put("messageId", MESSAGE_ID);
        map.put("createTime", CREATE_TIME);
        map.put("messageData", messageData);
        
        // 有交换机, 但该交换机没有绑定队列
        rabbitTemplate.convertAndSend("lonelyDirectExchange", "testDirectRouting", map);
        return "ok";
    }
}