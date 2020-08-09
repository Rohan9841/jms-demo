package com.rohan.jmsDemo.listener;

import java.util.UUID;

import javax.jms.JMSException;
import javax.jms.Message;

import org.springframework.jms.JmsException;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.rohan.jmsDemo.configuration.JmsConfig;
import com.rohan.jmsDemo.model.HelloWorldMessage;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MessageListener {

	private final JmsTemplate jmsTemplate;
	
	@JmsListener(destination = JmsConfig.MY_QUEUE)
	public void listen(@Payload HelloWorldMessage helloMessage, @Headers MessageHeaders header, Message message) {
//		System.out.println("I got message");
//		System.out.println(helloMessage);
	}
	
	@JmsListener(destination = JmsConfig.MY_SEND_RCV_QUEUE)
	public void listenForHelloRohan(@Payload HelloWorldMessage helloMessage, @Headers MessageHeaders header, Message message) throws JmsException, JMSException {
		HelloWorldMessage payLoadMessage = HelloWorldMessage
				.builder()
				.id(UUID.randomUUID())
				.message("Rohan Hello!")
				.build(); 
		 jmsTemplate.convertAndSend(message.getJMSReplyTo(),payLoadMessage);
	}
}
