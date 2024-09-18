package org.example.petwif.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.petwif.apiPayload.ApiResponse;
import org.example.petwif.apiPayload.exception.GeneralException;
import org.example.petwif.domain.entity.Member;
import org.example.petwif.repository.MemberRepository;
import org.example.petwif.service.MemberService.MailService;
import org.example.petwif.web.dto.MemberDto.EmailRequestDto;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static org.example.petwif.apiPayload.code.status.ErrorStatus._BAD_REQUEST;

@RestController
@RequiredArgsConstructor
//@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MailController {
    private final MailService emailVerificationService;
    private final MemberRepository memberRepository;

    /* Send Email: 인증번호 전송 버튼 click */
    @PostMapping("/signup/email")
    public ApiResponse<String> sendVerificationEmail(@RequestBody @Valid EmailRequestDto mail) {
        try{
            String email = mail.getEmail();
            Optional<Member> member = memberRepository.checkEmail(email, "PETWIF");
            if (member.isPresent()) {
                throw new IllegalStateException("Already assigned Member");
            }
            else emailVerificationService.sendVerificationEmail(email);
            return ApiResponse.onSuccess("Verification code sent to " + email);
        } catch (IllegalStateException e){
            return ApiResponse.onFailure("400", e.getMessage(), "다른 이메일로 가입해주세요");
        }
    }

    @PostMapping("/signup/pwFind")
    public ApiResponse<String> sendVerificationEmailForPW(@RequestBody @Valid EmailRequestDto mail) {
        try{
            String email = mail.getEmail();
            Optional<Member> member = memberRepository.checkEmail(email, "PETWIF");
            if (member.isPresent()) {
                emailVerificationService.sendVerificationEmail(email);
                return ApiResponse.onSuccess("Verification code sent to " + email);
            }
            else throw new IllegalStateException("가입되지 않은 이메일입니다. 회원가입을 먼저 해주세요.");
        } catch (IllegalStateException e){
            return ApiResponse.onFailure("400", e.getMessage(), "회원가입을 해주세요");
        }
    }

    @PostMapping("/verify")
    public ApiResponse<String> verifyCode(@RequestParam String email, @RequestParam String code) {
        System.out.println("메일 검증 ");
        boolean isVerified = emailVerificationService.verifyCode(email, code);
        if (isVerified) {
            return ApiResponse.onSuccess("Email verified successfully");
        } else {
            return ApiResponse.onFailure("400", "Email verify failed", code+" is not correct");
        }
    }
}

