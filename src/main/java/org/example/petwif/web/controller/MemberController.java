package org.example.petwif.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.petwif.JWT.TokenDto;
import org.example.petwif.S3.AmazonS3Manager;
import org.example.petwif.apiPayload.ApiResponse;
import org.example.petwif.domain.entity.Member;
import org.example.petwif.repository.MemberRepository;
import org.example.petwif.service.MemberService.MemberService;
//import org.example.petwif.service.StickerService.StickerService;
import org.example.petwif.web.dto.MemberDto.*;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
//@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MemberController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final AmazonS3Manager amazonS3Manager;

    @PostMapping("/register") // 동일한 이메일로 회원가입X, 비번 틀림 적용해서 회원가입 처리 완료
    public ApiResponse<EmailLoginResponse> registerNewMember(@RequestBody @Valid EmailSignupRequestDTO dto) {

        try{
            EmailLoginResponse response = memberService.EmailSignup(dto);
            if (response != null) {
                return ApiResponse.onSuccess(response);
            } else {
                return ApiResponse.onFailure("400", dto.getName() + ", 이미 회원입니다. 다른 이메일로 가입해주세요", null);
            }
        } catch (IllegalStateException e){
            return ApiResponse.onFailure("400", "wrong password", null);
        }
    }


    @PostMapping("/email/login")  // JWT 토큰을 생성하여 반환
    public ApiResponse<EmailLoginAccessTokenResponse> login(@RequestBody LoginRequestDto dto) {
        try {
            //TokenDto tokenDto = memberService.login(dto);
            EmailLoginAccessTokenResponse result = memberService.EmailLogin(dto);
            return ApiResponse.onSuccess(result);
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
                String nick = nickname.getNickname();
                return ApiResponse.onSuccess(nick+"으로 닉네임 변경 성공");
            } else {
                return ApiResponse.onFailure("400", "이미 사용중인 닉네임니다.", "duplicated nickname");
            }
        } catch(Exception e){
            return ApiResponse.onFailure("500", "error", "internal error");
        }
    }

    @PatchMapping("/nickname/beforeLogin")  // 닉네임 변경할 때 중복방지 및 변경 : 완료
    public ApiResponse<String> changeNicknameBeforeLogin(@RequestParam String email, @RequestBody NicknameDto nickname){
        Member member = memberRepository.findMemberByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("No member found with email: " + email));
        Long id = member.getId();
        try{
            if (memberService.checkNickName(id, nickname)){
                String nick = nickname.getNickname();
                return ApiResponse.onSuccess(nick+"으로 닉네임 변경 성공");
            } else {
                return ApiResponse.onFailure("400", "이미 사용중인 닉네임니다.", "duplicated nickname");
            }
        } catch(Exception e){
            return ApiResponse.onFailure("500", "error", "internal error");
        }
    }

    @PatchMapping("/addEtc/beforeLogin")
    public ApiResponse<String> addEtcInfoBeforeLogin(@RequestParam String email,
                                          @RequestBody MemberEtcInfoRequestDto dto) {
        Member member = memberRepository.findMemberByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("No member found with email: " + email));
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

    @PatchMapping("/addEtc")
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
    public ApiResponse<String> changePassword(@RequestParam String email, @Valid @RequestBody PasswordChangeRequestDto dto){
        Member member = memberRepository.findMemberByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("No member found with email: " + email));

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

    @DeleteMapping("/delete")
    public ApiResponse<String> deleteMember(@RequestParam("id") Long id){
        try {
            Member member = memberRepository.findByMemberId(id);
            memberService.deleteMember(id);
            return ApiResponse.onSuccess("id : "+ id +" , "+ member.getEmail()+"님 삭제 완료");
        } catch (Exception e) {
            return ApiResponse.onFailure("500", "존재하지 않는 회원입니다. ", null);
        }
    }

    @PostMapping(value="/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<String> uploadImage(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam("file") MultipartFile file) {

        Member member = memberService.getMemberByToken(authorizationHeader);
        Long memberId = member.getId();

        try {
            // S3에 파일 업로드 및 URL 반환
            String keyName =  file.getOriginalFilename()+ "/"+ memberId;
            String fileUrl = amazonS3Manager.uploadFile(keyName, file);

            memberService.uploadProfile(memberId, fileUrl);

            return ApiResponse.onSuccess(fileUrl);
        } catch (Exception e) {
            return ApiResponse.onFailure("400", "error", e.getMessage());
        }
    }

}