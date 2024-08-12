package org.example.petwif.service.MemberService;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender mailSender;
    private final Map<String, String> verificationCodes = new HashMap<>();

    public void sendVerificationEmail(String email) {
        // 인증번호 생성
        String code = generateVerificationCode();

        // 이메일 메시지 생성
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Your Verification Code");
        message.setText("Your verification code is: " + code);

        // 이메일 전송
        mailSender.send(message);

        // 인증번호를 메모리에 저장 (실제로는 Redis나 DB에 저장할 수 있지만 복잡해서 하다가 포기)
        verificationCodes.put(email, code);
    }

    public boolean verifyCode(String email, String code) {
        String storedCode = verificationCodes.get(email);
        System.out.println("stored code is " + storedCode);
        System.out.println("input code is " + code);
        System.out.println("비교 결과 " + storedCode.equals(code));
        return (storedCode != null && storedCode.equals(code));
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(999999); // 6자리 숫자 생성
        return String.format("%06d", code);
    }


}




