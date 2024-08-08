package org.example.petwif.web.controller;

import lombok.RequiredArgsConstructor;
import org.example.petwif.apiPayload.ApiResponse;
import org.example.petwif.service.MemberService.MailService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/signup")
public class MailController {
    private final MailService mailService;
    private int number; // 이메일 인증숫자 저장변수

    @PostMapping("/send")
    public ApiResponse<String> sendVerificationEmail(@RequestParam String email) {
        try {
            mailService.sendMail(email);
            return ApiResponse.onSuccess("Verification email sent.");//<>(true, "Verification email sent.", null);
        } catch (Exception e) {
            return null;//return new ApiResponse.onFailure("Failed to send verification email.");
        }
    }

    @PostMapping("/verify")
    public ApiResponse<String> verifyCode(@RequestParam String email, @RequestParam String code) {
        boolean isVerified = mailService.verifyCode(email, code);
        if (isVerified) {
            return ApiResponse.onSuccess("Verification successful.");
        } else {
            return null;//return ApiResponse.onFailure( "Verification fail");
        }
    }
}
