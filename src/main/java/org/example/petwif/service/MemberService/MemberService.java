package org.example.petwif.service.MemberService;

import lombok.RequiredArgsConstructor;
import org.example.petwif.domain.entity.Member;
import org.example.petwif.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private static MemberRepository memberRepository;
    private static PasswordEncoder passwordEncoder;

//    public Member registerMember(EmailRegistrationDto dto){
//        // 이메일 인증
//
//        // member 만들기 전에 먼저 비번 맞는지 체크!! 순서도 중요
//        if (!dto.getPw().equals(dto.getPwCheck())) { // 비번 체크 맞지 않으면 예외처리
//            throw new IllegalArgumentException("Passwords do not match");
//        }
//        if (memberRepository.findByEmail(dto.getEmail()) != null) {
//            throw new IllegalArgumentException("Email is already registered");
//        }
//
//        // 저장할때는 setter로 값 지정!!
//        Member member = new Member();
//        member.setName(dto.getName());
//        member.setEmail(dto.getEmail());
//        member.setPw(passwordEncoder.encode(dto.getPw()));
//
//        return memberRepository.save(member);
//    }
}
