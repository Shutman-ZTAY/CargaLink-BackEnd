package com.ipn.mx.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer{
	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) { //Broker nos ayuda a realizar la comunicacion entre el frontend al backend
		registry.enableSimpleBroker("/broker"); //Ruta por la cual se ingresa al broker
		registry.setApplicationDestinationPrefixes("/app"); //Por donde la aplicacion destina los mensajes
	}
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) { //Metodo que nos permite registrar los endpoints
		registry.addEndpoint("/socket")
			.setAllowedOrigins("http://localhost:4200")
			.withSockJS();
	}
}
