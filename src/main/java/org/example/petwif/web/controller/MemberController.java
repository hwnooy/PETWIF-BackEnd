package org.example.petwif.web.controller;

import lombok.RequiredArgsConstructor;
import org.example.petwif.apiPayload.ApiResponse;
import org.example.petwif.apiPayload.exception.GeneralException;
import org.example.petwif.domain.entity.Member;
import org.example.petwif.service.MemberService.MemberService;
import org.example.petwif.web.dto.LoginDto.EmailRegistrationDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.example.petwif.apiPayload.code.status.ErrorStatus._BAD_REQUEST;
import static org.example.petwif.apiPayload.code.status.SuccessStatus.OK;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private static MemberService memberService;
    @PostMapping("/register")
    public ApiResponse<Member> registerNewMember(@RequestBody EmailRegistrationDto dto){

        try{
            Member member = memberService.registerMember(dto);
            return ApiResponse.onSuccess(member);

        } catch (Exception e){
            throw new GeneralException(_BAD_REQUEST);
        }

    }

}
