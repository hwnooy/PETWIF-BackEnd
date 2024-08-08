package org.example.petwif.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender javaMailSender;
    private static final String senderEmail = "dreamznycloud@gmail.com";
    private static Map<String, String> verificationCodes = new HashMap<>();

    // 랜덤으로 숫자 생성하기 (인증번호)
    public static String createNumber() {
        int number = (int) (Math.random() * (900000)) + 100000; // 6자리 인증번호 생성
        return String.valueOf(number);
    }

    public void sendMail(String mail) {
        String number = createNumber();
        verificationCodes.put(mail, number);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderEmail);
        message.setTo(mail);
        message.setSubject("이메일 인증");
        message.setText("요청하신 PETWIF의 인증번호입니다: " + number + "\nPETWIF 인증페이지에서 해당 번호를 넣어주세요.");

        javaMailSender.send(message);
    }

    public boolean verifyCode(String email, String code) {
        String storedCode = verificationCodes.get(email);
        return storedCode != null && storedCode.equals(code);
    }
}
