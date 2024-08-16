package org.example.petwif.web.controller;

import lombok.RequiredArgsConstructor;
import org.example.petwif.JWT.TokenDto;
import org.example.petwif.apiPayload.ApiResponse;
import org.example.petwif.service.MemberService.SocialLogin.GoogleLoginService;
import org.example.petwif.service.MemberService.SocialLogin.GoogleTokenResponse;
import org.example.petwif.service.MemberService.SocialLogin.GoogleUserInfo;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
@RequestMapping(value = "/login/oauth2", produces = "application/json")
public class LoginController {

    private final GoogleLoginService googleLoginService;

    @PostMapping("/code/google")
    public ApiResponse<TokenDto> googleLogin(@RequestParam("code") String code) {
        try {
            TokenDto dto = googleLoginService.loginByGoogleAndSignUp(code);
            return ApiResponse.onSuccess(dto);
        } catch (Exception e) {
            return null;
        }
    }

}
