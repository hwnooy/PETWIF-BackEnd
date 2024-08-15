package org.example.petwif.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry){
        registry.addEndpoint("/ws") //클라이언트가 연결할 WebSocket 엔드포인트
                .setAllowedOrigins("*");
    }
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry){
        registry.enableSimpleBroker("/sub"); //서버에서 메시지를 전달할 때 사용하는 prefix
        registry.setApplicationDestinationPrefixes("/pub"); //클라이언트가 메시지를 보낼 때 사용하는 prefix
    }
}