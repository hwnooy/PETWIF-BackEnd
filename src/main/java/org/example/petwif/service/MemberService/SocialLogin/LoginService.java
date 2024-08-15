//package org.example.petwif.service.MemberService.SocialLogin;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.core.env.Environment;
//import org.springframework.http.*;
//import org.springframework.stereotype.Service;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.http.client.ClientHttpResponse;
//import org.springframework.web.client.HttpClientErrorException;
//import org.springframework.web.client.HttpServerErrorException;
//import org.springframework.web.client.RestClientException;
//import org.springframework.web.client.RestTemplate;
//
//@Service
//@Slf4j
//public class LoginService {
//
//    private final Environment env;
//    private final RestTemplate restTemplate;
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    public LoginService(Environment env) {
//        this.env = env;
//        this.restTemplate = new RestTemplate();
//    }
//
//    public void socialLogin(String code, String registrationId) {
//        log.info("======================================================");
//        String accessToken = getAccessToken(code, registrationId);
//        JsonNode userResourceNode = getUserResource(accessToken, registrationId);
//
//        UserResource userResource = new UserResource();
//        log.info("userResource = {}", userResource);
//        switch (registrationId) {
//            case "google": {
//                userResource.setId(userResourceNode.get("id").asText());
//                userResource.setEmail(userResourceNode.get("email").asText());
//                userResource.setNickname(userResourceNode.get("name").asText());
//                break;
//            } case "kakao": {
//                userResource.setId(userResourceNode.get("id").asText());
//                userResource.setEmail(userResourceNode.get("kakao_account").get("email").asText());
//                userResource.setNickname(userResourceNode.get("kakao_account").get("profile").get("nickname").asText());
//                break;
//            } case "naver": {
//                userResource.setId(userResourceNode.get("response").get("id").asText());
//                userResource.setEmail(userResourceNode.get("response").get("email").asText());
//                userResource.setNickname(userResourceNode.get("response").get("nickname").asText());
//                break;
//            } default: {
//                throw new RuntimeException("UNSUPPORTED SOCIAL TYPE");
//            }
//        }
//        log.info("id = {}", userResource.getId());
//        log.info("email = {}", userResource.getEmail());
//        log.info("nickname {}", userResource.getNickname());
//        log.info("======================================================");
//    }
//
//    private String getAccessToken(String authorizationCode, String registrationId) {
//        String clientId = env.getProperty("oauth2." + registrationId + ".client-id");
//        String clientSecret = env.getProperty("oauth2." + registrationId + ".client-secret");
//        String redirectUri = env.getProperty("oauth2." + registrationId + ".redirect-uri");
//        String tokenUri = "https://accounts.google.com/o/oauth2/auth?client_id=928539400314-fsf7hhtt5mbqvpa8slt5iae561c99mpc.apps.googleusercontent.com&redirect_uri=http://localhost:8080/login/oauth2/code/google&response_type=code&scope=https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile";
//                //env.getProperty("oauth2." + registrationId + ".token-uri");
//
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        params.add("code", authorizationCode);
//        params.add("client_id", clientId);
//        params.add("client_secret", clientSecret);
//        params.add("redirect_uri", redirectUri);
//        params.add("grant_type", "authorization_code");
//
//        HttpHeaders headers = new HttpHeaders();
//
//        restTemplate.getInterceptors().add((request, body, execution) -> {
//            ClientHttpResponse response = execution.execute(request,body);
//            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
//            return response;
//        });
//
//        HttpEntity entity = new HttpEntity(params, headers);
//
//        ResponseEntity<SocialResponseDto> responseEntity = restTemplate.exchange(
//                tokenUri, HttpMethod.POST, entity, SocialResponseDto.class);
//
//        SocialResponseDto tokenResponse = responseEntity.getBody();
//
//
//
//        return tokenResponse.getAccessToken();
//    }
//
//    private JsonNode getUserResource(String accessToken, String registrationId) {
//        String resourceUri = env.getProperty("oauth2."+registrationId+".resource-uri");
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", "Bearer " + accessToken);
//        HttpEntity entity = new HttpEntity(headers);
//        return restTemplate.exchange(resourceUri, HttpMethod.GET, entity, JsonNode.class).getBody();
//    }
//}
//
package org.example.petwif.service.MemberService.SocialLogin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.io.IOException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
@Slf4j
@RequiredArgsConstructor
public class LoginService {
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String googleClientSecret;
    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String googleRedirectUrl;

    public String loginByGoogle(String authorizationCode) throws Exception {
        // 1. Authorization Token과 Access Token 교환
        GoogleTokenResponse googleToken = getGoogleToken(authorizationCode);

        // 2. Access Token 이용하여 사용자 정보 획득
        GoogleUserInfo googleUserInfo = getGoogleUserInfo(googleToken.getAccessToken());

        log.info("Google User Info: {}", googleUserInfo);

        // 실제로 여기서 유저 정보를 DB에서 확인하고 로그인 처리하거나, 새로운 사용자를 등록합니다.
        return googleToken.getAccessToken();
    }

    /** Authorization Code를 이용하여 Google Access Token 발급 */
    private GoogleTokenResponse getGoogleToken(String authorizationCode) throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", authorizationCode);
        params.add("client_id", googleClientId);
        params.add("client_secret", googleClientSecret);
        params.add("redirect_uri", googleRedirectUrl);
        params.add("grant_type", "authorization_code");

        URI uri = UriComponentsBuilder
                .fromUriString("https://oauth2.googleapis.com/token")
                .build().toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return objectMapper.readValue(responseEntity.getBody(), GoogleTokenResponse.class);
        } else {
            throw new Exception("Failed to retrieve Google token");
        }
    }

    /** Access Token을 이용하여 사용자 정보 조회 */
    private GoogleUserInfo getGoogleUserInfo(String accessToken) throws Exception {
        URI uri = UriComponentsBuilder
                .fromUriString("https://www.googleapis.com/oauth2/v2/userinfo")
                .queryParam("access_token", accessToken)
                .build().toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return objectMapper.readValue(responseEntity.getBody(), GoogleUserInfo.class);
        } else {
            throw new Exception("Failed to retrieve Google user info");
        }
    }


    /* public void loginByGoogle(String authorizationCode) throws IOException, JsonProcessingException {
        // 1. AuthorizationToken 과 AccessToken 교환
        GoogleTokenResponse googleToken = getGoogleToken(authorizationCode);

        // 2. AccessToken 이용 사용자 정보 획득
        GoogleUserInfo googleUserInfo = getGoogleUserInfo(googleToken.getAccessToken());

        System.out.println(googleUserInfo);

    }//

    /** Authorization Token 이용 Google Token 발급 */
//    private GoogleTokenResponse getGoogleToken(String authorizationCode)
//            throws JsonProcessingException {
//        GoogleTokenRequest googleTokenRequest = GoogleTokenRequest
//                .create(authorizationCode, googleClientId, googleClientSecret, googleRedirectUrl);
//
//        URI getTokenUri = UriComponentsBuilder
//                .fromUriString("https://oauth2.googleapis.com")
//                .path("/token").encode().build().toUri();
//
//        ResponseEntity<String> googleResponse =
//                restTemplate.postForEntity(getTokenUri, googleTokenRequest, String.class);
//        return objectMapper.readValue(googleResponse.getBody(), GoogleTokenResponse.class);
//    }
//
//    /** Token 이용 사용자 정보 조회 */
//    private GoogleUserInfo getGoogleUserInfo(String accessToken) throws JsonProcessingException {
//        URI getTokenInfoUri = UriComponentsBuilder
//                .fromUriString("https://www.googleapis.com")
//                .path("/oauth2/v2/userinfo")
//                .queryParam("access_token", accessToken).encode().build().toUri();
//
//        ResponseEntity<String> googleTokenInfoResponse =
//                restTemplate.getForEntity(getTokenInfoUri, String.class);
//
//        return objectMapper.readValue(googleTokenInfoResponse.getBody(), GoogleUserInfo.class);
//    }
}
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.core.env.Environment;
//import org.springframework.http.*;
//import org.springframework.http.client.ClientHttpResponse;
//import org.springframework.stereotype.Service;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.client.HttpClientErrorException;
//import org.springframework.web.client.HttpServerErrorException;
//import org.springframework.web.client.RestTemplate;

//@Service
//@Slf4j
//public class LoginService {
//
//    private final Environment env;
//    private final RestTemplate restTemplate;
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    public LoginService(Environment env) {
//        this.env = env;
//        this.restTemplate = new RestTemplate();
//    }

//    public void socialLogin(String code, String registrationId) {
//        log.info("======================================================");
//        String accessToken = getAccessToken(code, registrationId);
//        JsonNode userResourceNode = getUserResource(accessToken, registrationId);
//
//        UserResource userResource = new UserResource();
//        log.info("userResource = {}", userResource);
//        switch (registrationId) {
//            case "google": {
//                userResource.setId(userResourceNode.get("id").asText());
//                userResource.setEmail(userResourceNode.get("email").asText());
//                userResource.setNickname(userResourceNode.get("name").asText());
//                break;
//            }
//            case "kakao": {
//                userResource.setId(userResourceNode.get("id").asText());
//                userResource.setEmail(userResourceNode.get("kakao_account").get("email").asText());
//                userResource.setNickname(userResourceNode.get("kakao_account").get("profile").get("nickname").asText());
//                break;
//            }
//            case "naver": {
//                userResource.setId(userResourceNode.get("response").get("id").asText());
//                userResource.setEmail(userResourceNode.get("response").get("email").asText());
//                userResource.setNickname(userResourceNode.get("response").get("nickname").asText());
//                break;
//            }
//            default: {
//                throw new RuntimeException("UNSUPPORTED SOCIAL TYPE");
//            }
//        }
//        log.info("id = {}", userResource.getId());
//        log.info("email = {}", userResource.getEmail());
//        log.info("nickname = {}", userResource.getNickname());
//        log.info("======================================================");
//    }
//
//    private String getAccessToken(String authorizationCode, String registrationId) {
//        String clientId = env.getProperty("oauth2." + registrationId + ".client-id");
//        String clientSecret = env.getProperty("oauth2." + registrationId + ".client-secret");
//        String redirectUri = env.getProperty("oauth2." + registrationId + ".redirect-uri");
//        String tokenUri = "https://accounts.google.com/o/oauth2/auth?client_id=928539400314-fsf7hhtt5mbqvpa8slt5iae561c99mpc.apps.googleusercontent.com&redirect_uri=http://localhost:8080/login/oauth2/code/google&response_type=code&scope=https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile";
//
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        params.add("code", authorizationCode);
//        params.add("client_id", clientId);
//        params.add("client_secret", clientSecret);
//        params.add("redirect_uri", redirectUri);
//        params.add("grant_type", "authorization_code");
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//        HttpEntity entity = new HttpEntity(params, headers);
//
//        ResponseEntity<JsonNode> responseNode = restTemplate.exchange(tokenUri, HttpMethod.POST, entity, JsonNode.class);
//        JsonNode accessTokenNode = responseNode.getBody();
//        return accessTokenNode.get("access_token").asText();


//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        params.add("code", authorizationCode);
//        params.add("client_id", clientId);
//        params.add("client_secret", clientSecret);
//        params.add("redirect_uri", redirectUri);
//        params.add("grant_type", "authorization_code");
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);
//
//        try {
//            ResponseEntity<SocialResponseDto> responseEntity = restTemplate.exchange(
//                    tokenUri, HttpMethod.POST, entity, SocialResponseDto.class);
//
//            SocialResponseDto tokenResponse = responseEntity.getBody();
//
//            if (tokenResponse != null) {
//                return tokenResponse.getAccessToken();
//            } else {
//                throw new RuntimeException("Failed to retrieve access token: Response body is null");
//            }
//        } catch (HttpClientErrorException | HttpServerErrorException ex) {
//            log.error("HTTP error response: {}", ex.getResponseBodyAsString());
//            throw new RuntimeException("Failed to fetch access token", ex);
//        } catch (Exception ex) {
//            log.error("Unexpected error: {}", ex.getMessage());
//            throw new RuntimeException("Failed to fetch access token", ex);
//


//    private JsonNode getUserResource(String accessToken, String registrationId) {
//        String resourceUri = env.getProperty("oauth2."+registrationId+".resource-uri");
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", "Bearer " + accessToken);
//        HttpEntity<String> entity = new HttpEntity<>(headers);
//
//        try {
//            ResponseEntity<JsonNode> responseEntity = restTemplate.exchange(resourceUri, HttpMethod.GET, entity, JsonNode.class);
//            return responseEntity.getBody();
//        } catch (HttpClientErrorException | HttpServerErrorException ex) {
//            log.error("HTTP error response: {}", ex.getResponseBodyAsString());
//            throw new RuntimeException("Failed to fetch user resource", ex);
//        } catch (Exception ex) {
//            log.error("Unexpected error: {}", ex.getMessage());
//            throw new RuntimeException("Failed to fetch user resource", ex);
//        }
//    }
//}
