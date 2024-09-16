package org.example.petwif.web.controller;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.example.petwif.apiPayload.ApiResponse;
import org.example.petwif.apiPayload.exception.GeneralException;
import org.example.petwif.domain.entity.Member;
import org.example.petwif.domain.entity.Pet;
import org.example.petwif.repository.MemberRepository;
import org.example.petwif.service.MemberService.MemberService;
import org.example.petwif.service.PetService.PetService;
import org.example.petwif.web.dto.PetDto.PetRequestDto;
import org.example.petwif.web.dto.PetDto.PetResponseDto;
import org.springframework.web.bind.annotation.*;
import static org.example.petwif.apiPayload.code.status.ErrorStatus._BAD_REQUEST;
import static org.example.petwif.apiPayload.code.status.SuccessStatus.OK;
import java.util.List;

@RestController
@RequestMapping("/pet")
@RequiredArgsConstructor
//@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PetController {

    private final PetService petService;
    private final MemberService memberService;
    private final MemberRepository memberRepository;

    /* 리스트로 조회하는 상황일 때 사용한 api */
    @GetMapping("/viewMyPets")
    public ApiResponse<List<PetResponseDto>> viewAllMyPets(@RequestHeader("Authorization")
                                          String authorizationHeader) {
        Member member = memberService.getMemberByToken(authorizationHeader);
        Long id = member.getId();
        try {
            return ApiResponse.onSuccess(petService.getAllPetList(id));
        } catch (Exception e) {
            throw new GeneralException(_BAD_REQUEST);
        }
    }

    @GetMapping("/view")
    public ApiResponse<PetResponseDto> viewMyPet(@RequestHeader("Authorization")
                                                           String authorizationHeader) {
        Member member = memberService.getMemberByToken(authorizationHeader);
        Long id = member.getId();
        try {
            return ApiResponse.onSuccess(petService.pet(id));
        } catch (Exception e) {
            throw new GeneralException(_BAD_REQUEST);
        }
    }

    /* 기존에 작성한 여러개 등록 api
    @PostMapping("/add")
    public ApiResponse<List<PetResponseDto>> newPet(@RequestHeader("Authorization")
                                                    String authorizationHeader,
                                                    @RequestBody List<PetRequestDto> dto) {
        Member member = memberService.getMemberByToken(authorizationHeader);
        Long id = member.getId();
        try {
            return ApiResponse.onSuccess(petService.addPet(id, dto));
        } catch (Exception e) {
            throw new GeneralException(_BAD_REQUEST);
        }
    }*/
    @PostMapping("/add")
    public ApiResponse<PetResponseDto> newPet(@RequestHeader("Authorization")
                                                    String authorizationHeader,
                                                    @RequestBody PetRequestDto dto) {
        Member member = memberService.getMemberByToken(authorizationHeader);
        Long id = member.getId();
        try {
            return ApiResponse.onSuccess(petService.addPet(id, dto));
        } catch (Exception e) {
            throw new GeneralException(_BAD_REQUEST);
        }
    }

    @PostMapping("/add/beforeLogin")
    public ApiResponse<PetResponseDto> newPetBeforeLogin(@RequestParam String email,
                                                               @RequestBody PetRequestDto dto) {
        Member member = memberRepository.findMemberByEmail(email).orElseThrow(()
                ->new IllegalArgumentException("No member with "+email));
        Long id = member.getId();
        try {
            return ApiResponse.onSuccess(petService.addPet(id, dto));
        } catch (Exception e) {
            throw new GeneralException(_BAD_REQUEST);
        }
    }

    @PatchMapping("/edit")
    public ApiResponse<PetResponseDto> editPet(@RequestHeader("Authorization")
                                       String authorizationHeader, @RequestBody PetRequestDto dto,
                                       @RequestParam("petId") Long petId){
        Member member = memberService.getMemberByToken(authorizationHeader);
        Long id = member.getId();

        try{
            return ApiResponse.onSuccess(petService.editPet(id, petId, dto));
        } catch (Exception e){
            throw new GeneralException(_BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete")
    public ApiResponse<String> deletePet(@RequestParam("id") Long id){
        try{
            petService.deletePet(id);
            return ApiResponse.onSuccess("id가 "+id+"인 Pet 삭제 완료");
        } catch (IllegalArgumentException e){
            return ApiResponse.onFailure("400", "error", e.getMessage());
        }
    }

}
