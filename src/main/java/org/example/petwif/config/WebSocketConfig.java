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
        registry.addEndpoint("/ws/chats") //클라이언트가 연결할 WebSocket 엔드포인트
                .setAllowedOrigins("*");
    }
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry){
        registry.enableSimpleBroker("/sub"); //메시지 구독 요청 = 메시지 송신
        registry.setApplicationDestinationPrefixes("/pub"); //메시지 발행 요청 = 메시지 수신
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        registration.setMessageSizeLimit(50 * 1024 * 1024); // 메세지 크기 제한 오류 방지(이 코드가 없으면 byte code를 보낼때 소켓 연결이 끊길 수 있음)
    }
}