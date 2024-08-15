package org.example.petwif.web.controller;

import lombok.RequiredArgsConstructor;
import org.example.petwif.apiPayload.ApiResponse;
import org.example.petwif.apiPayload.exception.GeneralException;
import org.example.petwif.domain.entity.Member;
import org.example.petwif.domain.entity.Pet;
import org.example.petwif.service.MemberService.MemberService;
import org.example.petwif.service.PetService.PetService;
import org.example.petwif.web.dto.PetDto.PetRequestDto;
import org.springframework.web.bind.annotation.*;
import static org.example.petwif.apiPayload.code.status.ErrorStatus._BAD_REQUEST;
import static org.example.petwif.apiPayload.code.status.SuccessStatus.OK;
import java.util.List;

@RestController
@RequestMapping("/pet")
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;
    private final MemberService memberService;

    @PostMapping("/add")
    public ApiResponse<String> newPet(@RequestHeader("Authorization")
                                          String authorizationHeader,
                                      @RequestParam Integer petNum, @RequestBody List<PetRequestDto> dto) {
        System.out.println("pet api 호출");
        Member member = memberService.getMemberByToken(authorizationHeader);
        Long id = member.getId();
        try {
            petService.addPet(id, dto);
            return ApiResponse.onSuccess(petNum+"명의 pet 등록완료");
        } catch (Exception e) {
            throw new GeneralException(_BAD_REQUEST);
        }
    }

/*
    @PatchMapping("/edit")
    public ApiResponse<String> editPet(@RequestHeader("Authorization")
                                        String authorizationHeader, @RequestBody List<PetRequestDto> dto){
        Member member = memberService.getMemberByToken(authorizationHeader);
        Long id = member.getId();
        System.out.println("pet edit api 호출");

        try{
            petService.editPet(id, dto);
        } catch (Exception e){

        }
    }
*/


//    @PatchMapping("/edit")
//    public ApiResponse<List<PetResponseDto>> editPet(){
//        return null;
//    }
//    @PostMapping("/newPet")
//    public ApiResponse<?> newPet(@RequestBody List<PetRequestDto> dtoList) {
//        System.out.print("pet API 실행");
//        try {
//            List<PetResponseDto> dto = petService.addPet(dtoList);
//            return ApiResponse.onSuccess(dto);
//        } catch (Exception e) {
//            throw new GeneralException(_BAD_REQUEST);
//        }
//    }



}
