package org.example.petwif.web.controller;

import lombok.RequiredArgsConstructor;
import org.example.petwif.apiPayload.ApiResponse;
import org.example.petwif.apiPayload.exception.GeneralException;
import org.example.petwif.domain.entity.Pet;
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
public class PetController {

    private final PetService petService;
    // private static MemberService memberService;

    @PostMapping("/add")
    public ApiResponse<String> newPet(@RequestParam Integer petNum, @RequestBody List<PetRequestDto> dto) {
        System.out.print("pet API 실행");
        try {
            petService.addPet(dto);
            return ApiResponse.onSuccess(petNum+" 명의 pet 등록완료");
        } catch (Exception e) {
            throw new GeneralException(_BAD_REQUEST);
        }
    }

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

    @PatchMapping("/edit")
    public ApiResponse<Pet> editPet(@RequestBody PetRequestDto dto){

        try{
            petService.editPet(dto);
            return null;
        } catch (Exception e){
            return null;
        }
    }

}
