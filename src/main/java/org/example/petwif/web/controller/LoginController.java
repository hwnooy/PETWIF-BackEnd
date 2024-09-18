package org.example.petwif.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.petwif.JWT.TokenDto;
import org.example.petwif.JWT.TokenProvider;
import org.example.petwif.apiPayload.ApiResponse;
import org.example.petwif.domain.entity.Member;
import org.example.petwif.repository.MemberRepository;
import org.example.petwif.service.MemberService.MemberService;
import org.example.petwif.service.MemberService.SocialLogin.GoogleLogin.GoogleLoginService;
import org.example.petwif.service.MemberService.SocialLogin.KakaoLogin.*;
import org.example.petwif.web.dto.MemberDto.EmailLoginAccessTokenResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
//@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
@RequestMapping(value = "/login/oauth2", produces = "application/json")
public class LoginController {

    private final GoogleLoginService googleLoginService;
    private final KakaoTokenJsonData kakaoTokenJsonData;
    private final KakaoUserInfo kakaoUserInfo;
    private final MemberService userService;
    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;

    @PostMapping("/code/google")
    @ResponseBody
    public ApiResponse<EmailLoginAccessTokenResponse> googleLogin(@RequestParam("code") String code) {
        try {
            TokenDto dto = googleLoginService.loginByGoogleAndSignUp(code);
            String accessToken = dto.getAccessToken();
            Member member = userService.getMemberByToken(accessToken);

            EmailLoginAccessTokenResponse result = userService.mapMemberToEmailResponse(dto,member);
            return ApiResponse.onSuccess(result);


        } catch (IllegalStateException e) {
            return ApiResponse.onFailure("code", e.getMessage(), null);
        } catch (Exception e) {
            return ApiResponse.onFailure("400", e.getMessage(), null);
        }
    }

    @GetMapping("/oauth")
    @ResponseBody
    public ApiResponse<EmailLoginAccessTokenResponse> kakaoOauth(@RequestParam("code") String code) {
        try{
            KakaoTokenResponse kakaoTokenResponse = kakaoTokenJsonData.getToken(code);
            KakaoUserInfoResponse userInfo = kakaoUserInfo.getUserInfo(kakaoTokenResponse.getAccess_token());
            log.info("회원 정보 입니다.{}", userInfo);
            KakaoAccount account = userInfo.getKakao_account();
            String email = account.getEmail();

            // 여기서 이메일 중복 확인하기
            Authentication authentication = new UsernamePasswordAuthenticationToken(email, null);
            TokenDto dto = tokenProvider.generateTokenDto(authentication);
            String accessToken = dto.getAccessToken();
            Member member = userService.getMemberByToken(accessToken);
            EmailLoginAccessTokenResponse result = userService.mapMemberToEmailResponse(dto,member);
            System.out.println("accessToken 확인 : " + dto.getAccessToken());
            userService.createUser(userInfo.getKakao_account().getEmail(), userInfo.getKakao_account().getProfile_image(), userInfo.getKakao_account().getProfile_nickname());

            return ApiResponse.onSuccess(result);
        } catch (Exception e){
            return ApiResponse.onFailure("code", e.getMessage(), null);
        }

    }

//    @PostMapping("/code/google")
//    @ResponseBody
//    public ApiResponse<TokenDto> googleLogin(@RequestParam("code") String code) {
//        try {
//            TokenDto dto = googleLoginService.loginByGoogleAndSignUp(code);
//
//            return ApiResponse.onSuccess(dto);
//        } catch (Exception e) {
//            return ApiResponse.onFailure("400", e.getMessage(), null);
//        }
//    }
//
//    @GetMapping("/oauth")
//    @ResponseBody
//    public ApiResponse<TokenDto> kakaoOauth(@RequestParam("code") String code) {
//        try{
//            KakaoTokenResponse kakaoTokenResponse = kakaoTokenJsonData.getToken(code);
//            KakaoUserInfoResponse userInfo = kakaoUserInfo.getUserInfo(kakaoTokenResponse.getAccess_token());
//            log.info("회원 정보 입니다.{}", userInfo);
//            KakaoAccount account = userInfo.getKakao_account();
//            String email = account.getEmail();
//
//            Authentication authentication = new UsernamePasswordAuthenticationToken(email, null);
//            TokenDto dto = tokenProvider.generateTokenDto(authentication);
//            System.out.println("accessToken 확인 : " + dto.getAccessToken());
//            userService.createUser(userInfo.getKakao_account().getEmail());
//
//            return ApiResponse.onSuccess(dto);
//        } catch (Exception e){
//            return ApiResponse.onFailure("code", e.getMessage(), null);
//        }
//
//    }


}
