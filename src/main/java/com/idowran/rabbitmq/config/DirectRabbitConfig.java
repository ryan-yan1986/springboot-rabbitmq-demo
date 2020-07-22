package com.idowran.rabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
 
/**
 * 交换机-路由工作模式
 * @Author : ryan
 * @CreateTime : 2020-07-21
 * @Description :
 **/
@Configuration
public class DirectRabbitConfig {
 
    // 队列 起名：testDirectQueue
    @Bean
    public Queue directQueue() {
        // durable: 是否持久化, 默认是true, 持久化队列: 会被存储在磁盘上，当消息代理重启时仍然存在; 暂存队列: 当前连接有效
        // exclusive: 默认是false, 只能被当前创建的连接使用, 而且当连接关闭后队列即被删除. 此参考优先级高于durable
        // autoDelete: 是否自动删除, 当没有生产者或者消费者使用此队列, 该队列会自动删除。
        //   return new Queue("testDirectQueue", true, false, false);
 
        // 一般设置一下队列的持久化就好, 其余两个就是默认false
        return new Queue("directQueue");
    }
 
    // Direct交换机 起名: testDirectExchange
    @Bean
    DirectExchange directExchange() {
    	// durable: 是否持久化, 默认是true, 持久化队列: 会被存储在磁盘上，当消息代理重启时仍然存在; 暂存队列: 当前连接有效
        // exclusive: 默认是false, 只能被当前创建的连接使用, 而且当连接关闭后队列即被删除. 此参考优先级高于durable
    	//  return new DirectExchange("testDirectExchange", true, true);
    	
    	// 一般设置一下队列的持久化就好
    	// 返回direct类型的交换机
        return new DirectExchange("directExchange");
    }

    // 绑定  将队列和交换机绑定, 并设置用于匹配键：TestDirectRouting
    @Bean
    Binding bindingDirectExchange() {
    	// 把队列绑定到交换机, 并设置路由key
        return BindingBuilder.bind(directQueue()).to(directExchange()).with("directRouting");
    }

    @Bean
    DirectExchange lonelyDirectExchange() {
    	// 定义一个交换机, 但是不绑定任何队列, 用于测试
        return new DirectExchange("lonelyDirectExchange");
    }
}