package org.example.petwif.service.MemberService;

import com.sun.jdi.request.InvalidRequestStateException;
import lombok.RequiredArgsConstructor;
import org.example.petwif.config.SecurityConfig;
import org.example.petwif.domain.entity.Member;
import org.example.petwif.repository.MemberRepository;
import org.example.petwif.web.controller.MailController;
import org.example.petwif.web.dto.LoginDto.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder encoder;

    @Transactional
    public void EmailSignup(EmailSignupRequestDTO dto){
        Member member = new Member();
        String pw1 = dto.getPw();
        String pw2 = dto.getPw_check();
        if (pw1.equals(pw2)) {
            member.setName(dto.getName());
            member.setEmail(dto.getEmail());
            member.setPw(encoder.encode(pw1));
            memberRepository.save(member);
        }

        else throw new InvalidRequestStateException("비밀번호가 일치하지 않습니다.");

    }

    public void MemberInfoAdd(Long memberId, MemberEtcInfoRequestDto dto){
        Member member = memberRepository.findByMemberId(memberId);
        member.setGender(dto.getGender());
        member.setBirthDate(dto.getBirthDate());
        member.setTelecom(dto.getTelecom());
        member.setPhoneNumber(dto.getPhoneNum());
        member.setAddress(dto.getAddress());
        memberRepository.save(member);

        System.out.println(member.getGender() +" test");
    }

    public void login(LoginRequestDto dto){

    }

    public void changePassword(Long id, PasswordChangeRequestDto dto){
        Member member = memberRepository.findByMemberId(id);

        String pw1 = dto.getChangePW();
        String pw2 = dto.getCheckChangePw();

        if (pw1.equals(pw2)){
            member.setPw(encoder.encode(pw1));
        }

    }
}
