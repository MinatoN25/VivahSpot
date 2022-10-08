package com.stackroute.booking.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfig {
    
    public static final String REFUND_QUEUE = "refund_queue";
    public static final String REFUND_EXCHANGE = "refund_exchange";
    public static final String REFUND_ROUTING_KEY = "refund_routingKey";
    
    @Bean
    public Queue invoiceQueue() {
        return  new Queue(REFUND_QUEUE);
    }

    @Bean
    public TopicExchange invoiceExchange() {
        return new TopicExchange(REFUND_EXCHANGE);
    }

    @Bean
    public Binding invoiceBinding(Queue queue, TopicExchange exchange) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with(REFUND_ROUTING_KEY);
    }

    @Bean
    public MessageConverter messageConverter() {
        return  new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate template(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return  template;
    }

}