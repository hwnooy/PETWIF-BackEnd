package org.example.petwif.service.PetService;

import lombok.RequiredArgsConstructor;
import org.example.petwif.domain.entity.Member;
import org.example.petwif.domain.entity.Pet;
import org.example.petwif.repository.MemberRepository;
import org.example.petwif.repository.PetRepository;
import org.example.petwif.web.dto.PetDto.PetRequestDto;
import org.example.petwif.web.dto.PetDto.PetResponseDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PetService {
    private final PetRepository petRepository;
    private final MemberRepository memberRepository;
    // id 매칭을 위한 accessToken -> member repository에서 id 가져오기

    public List<Pet> addPet(List<PetRequestDto> dtoList){
        // 고민 -> n마리를 넣고 n개를 넘겨야하는데 이걸 어케 넘기지...
        // 해당 로그인한 사람의 id에 펫을 추가해야함
        //Member member = memberRepository.findById();
        List<Pet> savedPets = new ArrayList<>();

        for (PetRequestDto dto : dtoList){
            Pet pet = new Pet();
            pet.setPetAge(dto.getAge());
            pet.setPetGender(dto.getGender());
            pet.setPetName(dto.getPetName());
            pet.setPetKind(dto.getPetKind());
            savedPets.add(petRepository.save(pet));
        }
        System.out.println("addPet called with dtoList: " + dtoList);
        return savedPets;
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
