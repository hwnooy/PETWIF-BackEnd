package org.example.petwif.web.controller;

import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.Token;
import org.example.petwif.JWT.TokenDto;
import org.example.petwif.apiPayload.ApiResponse;
import org.example.petwif.apiPayload.code.BaseErrorCode;
import org.example.petwif.apiPayload.exception.GeneralException;
import org.example.petwif.service.MemberService.SocialLogin.GoogleTokenResponse;
import org.example.petwif.service.MemberService.SocialLogin.LoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
@RequestMapping(value = "/login/oauth2", produces = "application/json")
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/code/google")
    public ApiResponse<TokenDto> googleLogin(@RequestParam("code") String code) {
        try {
            TokenDto dto = loginService.loginByGoogle(code);
            return ApiResponse.onSuccess(dto);
        } catch (Exception e) {
            return null;
        }
    }

}
