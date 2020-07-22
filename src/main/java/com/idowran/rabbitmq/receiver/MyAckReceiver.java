package com.idowran.rabbitmq.receiver;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;

/**
 * 手动确认消息监听类
 * @author ryan
 *
 */
@Component
public class MyAckReceiver implements ChannelAwareMessageListener{

	@Override
	public void onMessage(Message message, Channel channel) throws Exception {
		// TODO Auto-generated method stub
		MessageProperties properties = message.getMessageProperties();
		long deliveryTag = properties.getDeliveryTag();
        
		try {
            // 因为传递消息的时候用的map传递, 所以将Map从Message内取出需要做些处理
			String msg = message.toString();
            String[] msgArray = msg.split("'");// 可以点进Message里面看源码, 单引号直接的数据就是我们的map消息数据
            Map<String, String> msgMap = mapStringToMap(msgArray[1].trim());
            
            for (String key : msgMap.keySet()) {
            	System.out.println("MyAckReceiver -- " + key + ": " + msgMap.get(key));
			}
            
            // 监听多个队列, 那么可以根据队列名来做不同的处理
            String queue = properties.getConsumerQueue();
            if(queue.equals("testDirectQueue")) {
            	System.out.println("消费的消息来自: " + properties.getReceivedExchange() + " 交换机");
                System.out.println("消费的消息来自: " + properties.getConsumerQueue() + " 队列");
                System.out.println("消费的消息来自: " + properties.getReceivedRoutingKey() + "路由");
            }
            if(queue.equals("fanout.A")) {
            	System.out.println("消费的消息来自: " + properties.getReceivedExchange() + " 交换机");
                System.out.println("消费的消息来自: " + properties.getConsumerQueue() + " 队列");
                System.out.println("消费的消息来自: " + properties.getReceivedRoutingKey() + "路由");
            }
            if(queue.equals("fanout.B")) {
            	System.out.println("消费的消息来自: " + properties.getReceivedExchange() + " 交换机");
                System.out.println("消费的消息来自: " + properties.getConsumerQueue() + " 队列");
                System.out.println("消费的消息来自: " + properties.getReceivedRoutingKey() + "路由");
            }
            
            
            channel.basicAck(deliveryTag, true);
//			channel.basicReject(deliveryTag, true);	// 为true会重新放回队列
        } catch (Exception e) {
            channel.basicReject(deliveryTag, false);
            e.printStackTrace();
        }
	}

	
	//{key = value, key = value, key = value} 格式转换成map
    private Map<String, String> mapStringToMap(String str) {
        str = str.substring(1, str.length() - 1);
        String[] strs = str.split(",");
        
        Map<String, String> map = new LinkedHashMap<>();
        for (String string : strs) {
        	String[] arr 	= string.split("=");
    		String key 		= arr[0].trim();
    		String value 	= arr.length > 1 ? arr[1] : "[nothing]";
            map.put(key, value);
        }
        return map;
    }
}
