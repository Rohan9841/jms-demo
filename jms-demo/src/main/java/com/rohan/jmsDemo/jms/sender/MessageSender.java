package com.rohan.jmsDemo.jms.sender;

import java.util.UUID;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rohan.jmsDemo.configuration.JmsConfig;
import com.rohan.jmsDemo.model.HelloWorldMessage;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MessageSender {

	final JmsTemplate jmsTemplate;
	final ObjectMapper objectMapper;

	@Scheduled(fixedRate = 2000)
	public void sendMessage() {	
		HelloWorldMessage message = HelloWorldMessage
				.builder()
				.id(UUID.randomUUID())
				.message("Hello World!")
				.build(); 
		
		jmsTemplate.convertAndSend(JmsConfig.MY_QUEUE, message);
	}
	
	@Scheduled(fixedRate = 2000)
	public void sendAndReceiveMessage() throws JMSException {
		HelloWorldMessage message = HelloWorldMessage
				.builder()
				.id(UUID.randomUUID())
				.message("Hello Rohan!")
				.build(); 
		
		Message receivedMsg = jmsTemplate.sendAndReceive(JmsConfig.MY_SEND_RCV_QUEUE, new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				Message helloMessage = null;
				try {
					helloMessage = session.createTextMessage(objectMapper.writeValueAsString(message));
					helloMessage.setStringProperty("_type","com.rohan.jmsDemo.model.HelloWorldMessage");
					
					System.out.println("Sending Hello Rohan!");
					return helloMessage;
				} catch (Exception e) {
					throw new JMSException("boom");
				}
				
			}
		});
		System.out.println(receivedMsg.getBody(String.class));
	}
}
