package org.example.petwif.web.controller;

import lombok.RequiredArgsConstructor;
import org.example.petwif.apiPayload.ApiResponse;
import org.example.petwif.apiPayload.exception.GeneralException;
import org.example.petwif.domain.enums.Gender;
import org.example.petwif.domain.enums.Telecom;
import org.example.petwif.service.MemberService.MemberService;
import org.example.petwif.web.dto.LoginDto.EmailSignupRequestDTO;
import org.example.petwif.web.dto.LoginDto.LoginRequestDto;
import org.example.petwif.web.dto.LoginDto.MemberEtcInfoRequestDto;
import org.example.petwif.web.dto.LoginDto.PasswordChangeRequestDto;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/register")  // 이건 완료
    public ApiResponse<String> registerNewMember(@RequestBody EmailSignupRequestDTO dto) {
        try {
            memberService.EmailSignup(dto);
            return ApiResponse.onSuccess(dto.getName() + "님 " + dto.getEmail() + "으로 회원가입 성공하였습니다.");
        } catch (Exception e) {
            return ApiResponse.onFailure("400", dto.getName()+"님 회원가입 오류입니다. 다시 시도해주세요", "wrong password");
        }
    }

    @PostMapping("/login")
    public ApiResponse<String> login(@RequestBody LoginRequestDto dto){
        try{
            memberService.login(dto);
            return ApiResponse.onSuccess("로그인 성공");
        } catch (Exception e){
            return ApiResponse.onFailure("400", "로그인 실패", "로그인 실패");
        }
    }

    @PatchMapping("/addEtc")
    public ApiResponse<String> addEtcInfo(@RequestParam Long memberId, @RequestBody MemberEtcInfoRequestDto dto){
        System.out.println("member addEtc api 호출");
        try{
            memberService.MemberInfoAdd(memberId, dto);
            return ApiResponse.onSuccess("회원정보 수정완료");
        } catch (Exception e){
            return ApiResponse.onFailure("400", "회원을 찾을 수 없습니다.", "회원정보 추가 실패");
        }
    }

    @PostMapping("/change/pw")
    public ApiResponse<String> changePassword(@RequestParam Long id, @RequestBody PasswordChangeRequestDto dto){
        try {
            memberService.changePassword(id, dto);
            return ApiResponse.onSuccess("비밀번호 바꾸기 완료");
        } catch (Exception e){
            return ApiResponse.onFailure("400", "비밀번호 틀림", "비밀번호 수정 실패");
        }

    }

}