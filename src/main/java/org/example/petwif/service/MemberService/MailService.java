package org.example.petwif.service.MemberService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.example.petwif.config.RedisConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;
    private final RedisConfig redisConfig;
    private int authNumber;

    /* 이메일 인증에 필요한 정보 */
    @Value("${spring.mail.username}")
    private String serviceName;

    /* 랜덤 인증번호 생성 */
    public void makeRandomNum() {
        Random r = new Random();
        String randomNumber = "";
        for(int i = 0; i < 6; i++) {
            randomNumber += Integer.toString(r.nextInt(10));
        }

        authNumber = Integer.parseInt(randomNumber);
    }

    /* 이메일 전송 */
    public void mailSend(String setFrom, String toMail, String title, String content) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message,true,"utf-8");
            helper.setFrom(setFrom); // service name
            helper.setTo(toMail); // customer email
            helper.setSubject(title); // email title
            helper.setText(content,true); // content, html: true
            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace(); // 에러 출력
        }
        // redis에 3분 동안 이메일과 인증 코드 저장
        ValueOperations<String, String> valOperations = redisConfig.redisTemplate().opsForValue();
        valOperations.set(toMail, Integer.toString(authNumber), 180, TimeUnit.SECONDS);
    }

    /* 이메일 작성 */
    public String joinEmail(String email) {
        makeRandomNum();
        String customerMail = email;
        String title = "회원 가입을 위한 이메일입니다!";
        String content =
                "이메일을 인증하기 위한 절차입니다." +
                        "<br><br>" +
                        "인증 번호는 " + authNumber + "입니다." +
                        "<br>" +
                        "회원 가입 폼에 해당 번호를 입력해주세요.";
        mailSend(serviceName, customerMail, title, content);
        return Integer.toString(authNumber);
    }
}

//@Service
//@RequiredArgsConstructor
//@Transactional(readOnly = true)
//public class MailService{
//
//
//    private final JavaMailSender javaMailSender;
//    private final RedisTemplate<String, String> redisTemplate;
//
//    @Value("${spring.mail.username}")
//    private String serviceName;
//
//    private String generateAuthCode() {
//        Random random = new Random();
//        StringBuilder code = new StringBuilder();
//        for (int i = 0; i < 6; i++) {
//            code.append(random.nextInt(10));
//        }
//        return code.toString();
//    }
//
//    public void sendVerificationEmail(String toEmail) {
//        String authCode = generateAuthCode();
//
//        MimeMessage message = javaMailSender.createMimeMessage();
//        try {
//            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
//            helper.setFrom(serviceName);
//            helper.setTo(toEmail);
//            helper.setSubject("Your Email Verification Code");
//            helper.setText("Your verification code is: " + authCode, true);
//            javaMailSender.send(message);
//
//            // 인증 코드를 Redis에 저장, 3분 동안 유효
//            ValueOperations<String, String> valOps = redisTemplate.opsForValue();
//            valOps.set(toEmail, authCode, 3, TimeUnit.MINUTES);
//
//        } catch (MessagingException e) {
//            e.printStackTrace();
//            throw new IllegalArgumentException("Failed to send email");
//        }
//    }
//
//    public boolean verifyCode(String email, String code) {
//        ValueOperations<String, String> valOps = redisTemplate.opsForValue();
//        String storedCode = valOps.get(email);
//        return storedCode != null && storedCode.equals(code);
//    }
//}

//@Service
//@RequiredArgsConstructor
//public class MailService {
//    private final JavaMailSender javaMailSender;
//    private static final String senderEmail = "dreamznycloud@gmail.com";
//    private static int number;
//
//    // 랜덤으로 숫자 생성
//    public static void createNumber() {
//        number = (int)(Math.random() * (90000)) + 100000; //(int) Math.random() * (최댓값-최소값+1) + 최소값
//    }
//
//    public MimeMessage CreateMail(String mail) {
//        createNumber();
//        MimeMessage message = javaMailSender.createMimeMessage();
//
//        try {
//            message.setFrom(senderEmail);
//            message.setRecipients(MimeMessage.RecipientType.TO, mail);
//            message.setSubject("이메일 인증");
//            String body = "";
//            body += "<h3>" + "요청하신 인증 번호입니다." + "</h3>";
//            body += "<h1>" + number + "</h1>";
//            body += "<h3>" + "감사합니다." + "</h3>";
//            message.setText(body,"UTF-8", "html");
//        } catch (MessagingException e) {
//            e.printStackTrace();
//        }
//
//        return message;
//    }
//
//    public int sendMail(String mail) {
//        MimeMessage message = CreateMail(mail);
//        javaMailSender.send(message);
//
//        return number;
//    }
//}


