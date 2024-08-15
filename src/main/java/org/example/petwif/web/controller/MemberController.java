package org.example.petwif.web.controller;

import lombok.RequiredArgsConstructor;
import org.example.petwif.JWT.TokenDto;
import org.example.petwif.apiPayload.ApiResponse;
import org.example.petwif.apiPayload.exception.GeneralException;
import org.example.petwif.domain.entity.Member;
import org.example.petwif.service.MemberService.MemberService;
import org.example.petwif.web.dto.MemberDto.*;
import org.springframework.web.bind.annotation.*;

import static org.example.petwif.apiPayload.code.status.ErrorStatus._BAD_REQUEST;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/register") // 동일한 이메일로 회원가입X, 비번 틀림 적용해서 회원가입 처리 완료
    public ApiResponse<String> registerNewMember(@RequestBody EmailSignupRequestDTO dto) {
        try {
            if (memberService.EmailSignup(dto)) {
                return ApiResponse.onSuccess(dto.getName() + "님 " + dto.getEmail() + "으로 회원가입 성공하였습니다.");
            } else {
                return ApiResponse.onFailure("400", dto.getName() + ", 이미 회원입니다. 다른 이메일로 가입해주세요", "duplicated email");
            }
        } catch (IllegalArgumentException e) {
            return ApiResponse.onFailure("400", "비밀번호가 일치하지 않습니다.", "wrong password");
        } catch (Exception e) {
            return ApiResponse.onFailure("500", dto.getName() + "님 회원가입 오류입니다. 다시 시도해주세요", "internal error");
        }
    }


    @PostMapping("/email/login")  // JWT 토큰을 생성하여 반환
    public ApiResponse<TokenDto> login(@RequestBody LoginRequestDto dto) {
        try {
            TokenDto tokenDto = memberService.login(dto);
            return ApiResponse.onSuccess(tokenDto);
        } catch (IllegalArgumentException e) {
            return ApiResponse.onFailure("400", e.getMessage(), null);
        } catch (Exception e) {
            return ApiResponse.onFailure("500", "다시하세요",null);
        }
    }

    @PatchMapping("/nickname")  // 닉네임 변경할 때 중복방지 및 변경 : 완료
    public ApiResponse<String> changeNickname(@RequestHeader("Authorization") String authorizationHeader,
                                              @RequestBody NicknameDto nickname){
        Member member = memberService.getMemberByToken(authorizationHeader);
        Long id = member.getId();
        try{
            if (memberService.checkNickName(id, nickname)){
                return ApiResponse.onSuccess("닉네임 변경 성공");
            } else {
                return ApiResponse.onFailure("400", "이미 사용중인 닉네임니다.", "duplicated nickname");
            }
        } catch(Exception e){
            return ApiResponse.onFailure("500", "error", "internal error");
        }
    }

    @PatchMapping("/addEtc")  // 이것도 완료, 나중에 accessToken 처리하기
    public ApiResponse<String> addEtcInfo(@RequestHeader("Authorization") String authorizationHeader,
                                          @RequestBody MemberEtcInfoRequestDto dto) {
        Member member = memberService.getMemberByToken(authorizationHeader);
        Long memberId = member.getId();
        try {
            boolean isUpdated = memberService.MemberInfoAdd(memberId, dto);
            if (isUpdated) {
                return ApiResponse.onSuccess("회원정보 수정완료");
            } else {
                return ApiResponse.onFailure("404", "회원을 찾을 수 없습니다.", "회원정보 추가 실패");
            }
        } catch (Exception e) {
            return ApiResponse.onFailure("500", "서버 오류가 발생했습니다.", "회원정보 추가 실패");
        }
    }


    @PatchMapping("/change/pw")   // 이것도 완료
    public ApiResponse<String> changePassword(@RequestHeader("Authorization") String authorizationHeader,
                                              @RequestBody PasswordChangeRequestDto dto){
        Member member = memberService.getMemberByToken(authorizationHeader);
        Long id = member.getId();
        try {
            if (memberService.changePassword(id, dto)) return ApiResponse.onSuccess("비밀번호 바꾸기 완료");
            else return ApiResponse.onFailure("400", "비밀번호 틀림", "비밀번호 수정 실패");

        } catch (Exception e){
            return ApiResponse.onFailure("400", "비밀번호 틀림", "비밀번호 수정 실패");
        }
    }

    @GetMapping("/me")
    public ApiResponse<Member> getMemberByToken(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader; // Bearer 제거하지 않음
        try {
            Member member = memberService.getMemberByToken(token);
            return ApiResponse.onSuccess(member);
        } catch (IllegalArgumentException e) {
            return ApiResponse.onFailure("400", e.getMessage(), null);
        } catch (Exception e) {
            return ApiResponse.onFailure("500", "회원 정보를 가져오는 중 오류가 발생했습니다.", null);
        }
    }
}