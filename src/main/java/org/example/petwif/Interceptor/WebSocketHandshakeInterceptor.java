package org.example.petwif.Interceptor;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
public class WebSocketHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        // ServerHttpRequest에서 헤더 가져오기
        String authHeader = request.getHeaders().getFirst("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwtToken = authHeader.substring(7);

            // JWT 검증 로직 (ex: 유효성 검사 및 사용자 정보 확인)
            // 검증 성공 시 attributes에 사용자 정보 저장
            // attributes.put("user", userDetails);

            return true;  // 검증 성공 시 true 반환
        }

        return false;  // 검증 실패 시 false 반환
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception ex) {
        // 핸드셰이크 이후 로직 (필요 시 구현)
    }
}
