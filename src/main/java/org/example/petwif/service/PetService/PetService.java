package org.example.petwif.service.PetService;

import lombok.RequiredArgsConstructor;
import org.example.petwif.domain.entity.Member;
import org.example.petwif.domain.entity.Pet;
import org.example.petwif.repository.MemberRepository;
import org.example.petwif.repository.PetRepository;
import org.example.petwif.web.dto.PetDto.PetRequestDto;
import org.example.petwif.web.dto.PetDto.PetResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PetService {
    private final PetRepository petRepository;
    //private final MemberRepository memberRepository;
    // id 매칭을 위한 accessToken -> member repository에서 id 가져오기

    public void addPet(PetRequestDto dto){
//        List<Pet> savedPets = new ArrayList<>();
            Pet pet = new Pet();
            pet.setPetAge(dto.getAge());
            pet.setPetGender(dto.getGender());
            pet.setPetName(dto.getPetName());
            pet.setPetKind(dto.getPetKind());
            System.out.println("pet : "+ pet.getPetKind()+" "+ pet.getPetName());
            petRepository.save(pet);
    }



    public Pet editPet(PetRequestDto dto){
        //Member member = memberRepository.findById();
        Pet pet = new Pet();
        pet.setPetKind(dto.getPetKind());
        pet.setPetAge(dto.getAge());
        pet.setPetName(dto.getPetName());
        pet.setPetGender(dto.getGender());
        return pet;
        //pet.patch(pet);
    }

    //private void patch(Pet pet){
//        if (pet.getPetName()!=null) pet.set
//    }
    //public void  deletePet


}
