package org.example.petwif.interceptor;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.security.auth.message.AuthException;
import lombok.SneakyThrows;
import org.example.petwif.JWT.TokenProvider;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class WebSocketInterceptor implements ChannelInterceptor {
    private TokenProvider tokenProvider;

    @SneakyThrows
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor.getCommand() == StompCommand.CONNECT) {
            String authToken = accessor.getFirstNativeHeader("Authorization");

            if (authToken == null) {
                throw new AuthException("Authentication failed!!");
            }

            // UsernamePasswordAuthenticationToken 발급
            Authentication authentication = tokenProvider.getAuthentication(authToken);
            // accessor에 등록
            accessor.setUser(authentication);

        }
        return message;
    }
}
