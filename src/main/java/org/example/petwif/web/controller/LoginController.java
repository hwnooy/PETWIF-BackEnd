package org.example.petwif.web.controller;

import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.Token;
import org.example.petwif.JWT.TokenDto;
import org.example.petwif.apiPayload.ApiResponse;
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

    @GetMapping("/code/google")
    public String googleLogin(@RequestParam("code") String code) {
        try {
            String accessToken = loginService.loginByGoogle(code);
            return "Success! Access Token: " + accessToken;
        } catch (Exception e) {
            return "Failed to login with Google: " + e.getMessage();
        }
    }

}
