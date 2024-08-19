package org.example.petwif.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    public static final String ALLOWED_METHOD_NAMES = "GET,HEAD,POST,PUT,DELETE,TRACE,OPTIONS,PATCH";
    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173") // 허용할 출처
                .allowedMethods(ALLOWED_METHOD_NAMES) // 허용할 HTTP method
                .allowCredentials(true); // 쿠키 인증 요청 허용
                 // 원하는 시간만큼 pre-flight 리퀘스트를 캐싱
    }
}