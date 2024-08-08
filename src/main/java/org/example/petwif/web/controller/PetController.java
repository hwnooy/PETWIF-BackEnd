package org.example.petwif.web.controller;

import lombok.RequiredArgsConstructor;
import org.example.petwif.apiPayload.ApiResponse;
import org.example.petwif.domain.entity.Pet;
import org.example.petwif.service.PetService.PetService;
import org.example.petwif.web.dto.PetDto.PetRequestDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/pet")
@RequiredArgsConstructor
public class PetController {

    private static PetService petService;
    // private static MemberService memberService;
    @PostMapping("/newPet")
    public ApiResponse<Pet> NewPet(@RequestBody PetRequestDto dto){
        try{
            petService.addPet(dto);
            return
        } catch (Exception e){
            return
        }
    }

    @PatchMapping("/edit")
    public ApiResponse<Pet> editPet(@RequestBody PetRequestDto dto){

        try{
            petService.editPet(dto);
            return ;
        } catch (Exception e){
            return ;
        }
    }

}
