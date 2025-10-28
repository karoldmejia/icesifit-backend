package com.example.physical_activity_project.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker  // Habilita el uso de WebSockets con STOMP
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Canales donde los clientes pueden "escuchar" mensajes (salida)
        config.enableSimpleBroker("/topic");

        // Prefijo para los mensajes que vienen del cliente (entrada)
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Endpoint donde los clientes se conectan
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS();
    }
}
