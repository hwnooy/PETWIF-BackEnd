package org.example.petwif.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.petwif.service.MemberService.MailService;
import org.example.petwif.web.dto.LoginDto.EmailRequestDto;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
@RestController
@RequiredArgsConstructor
public class MailController {
    private final MailService emailSendService;

    /* Send Email: 인증번호 전송 버튼 click */
    @PostMapping("/signup/email")
    public Map<String, String> mailSend(@RequestBody @Valid EmailRequestDto emailRequestDto) {
        System.out.println("email");
        String code = emailSendService.joinEmail(emailRequestDto.getEmail());
        // response를 JSON 문자열으로 반환
        Map<String, String> response = new HashMap<>();
        response.put("code", code);

        return response;
    }
}

//    @PostMapping("/verify")
//    public String verifyCode(@RequestParam String email, @RequestParam String code) {
//        boolean isVerified = emailSendService.verifyCode(email, code);
//        return isVerified ? "Verification successful" : "Invalid verification code";
//    }

//@Slf4j
//public class MailController {
//
//
//    private final MailService mailService;
//    private int number; // 이메일 인증 숫자를 저장하는 변수
//
//    // 인증 이메일 전송
//    @PostMapping("/mailSend")
//    public HashMap<String, Object> mailSend(@RequestParam String mail) {
//        System.out.println("mail");
//        HashMap<String, Object> map = new HashMap<>();
//
//        try {
//            number = mailService.sendMail(mail);
//            String num = String.valueOf(number);
//
//            map.put("success", Boolean.TRUE);
//            map.put("number", num);
//        } catch (Exception e) {
//            map.put("success", Boolean.FALSE);
//            map.put("error", e.getMessage());
//        }
//
//        return map;
//    }
//
//    // 인증번호 일치여부 확인
//    @GetMapping("/mailCheck")
//    public ApiResponse<?> mailCheck(@RequestParam String userNumber) {
//
//        boolean isMatch = userNumber.equals(String.valueOf(number));
//
//        return ApiResponse.onSuccess(isMatch);
//    }
//}
//@Controller
//@RequiredArgsConstructor
//@RequestMapping("/signup")
//public class MailController {
//    private final MailService mailService;
//    private int number; // 이메일 인증숫자 저장변수
//
//    @PostMapping("/send")
//    public ApiResponse<String> sendVerificationEmail(@RequestParam String email) {
//        System.out.println("email");
//        try {
//            mailService.sendMail(email);
//            return ApiResponse.onSuccess("Verification email sent.");//<>(true, "Verification email sent.", null);
//        } catch (Exception e) {
//            return null;//return new ApiResponse.onFailure("Failed to send verification email.");
//        }
//    }
//
//    @PostMapping("/verify")
//    public ApiResponse<String> verifyCode(@RequestParam String email, @RequestParam String code) {
//        boolean isVerified = mailService.verifyCode(email, code);
//        if (isVerified) {
//            return ApiResponse.onSuccess("이메일 인증 완료");
//        } else {
//            return null;
//        }
//    }
//}
