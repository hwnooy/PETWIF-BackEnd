package org.example.petwif.service.MemberService.SocialLogin.KakaoLogin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Component
@RequiredArgsConstructor
public class KakaoTokenJsonData {
    private final WebClient webClient;
    private static final String TOKEN_URI = "https://kauth.kakao.com/oauth/token";
    private static final String REDIRECT_URI = "http://localhost:8080/kakaoLogin";
    private static final String GRANT_TYPE = "authorization_code";
    private static final String CLIENT_ID = "290e9622e67ae7945bf3ba677f42dc48"; // Replace with your client ID
    private static final String CLIENT_SECRET = "LW0baB7Koy6ILrW1OyygT6VtS1tyWzI9"; // Replace with your client secret

    public KakaoTokenResponse getToken(String code) {
        return webClient.post()
                .uri(TOKEN_URI)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("grant_type", GRANT_TYPE)
                        .with("client_id", CLIENT_ID)
                        .with("redirect_uri", REDIRECT_URI)
                        .with("code", code)
                        .with("client_secret", CLIENT_SECRET))
                .retrieve()
                .bodyToMono(KakaoTokenResponse.class)
                .block();
    }
}