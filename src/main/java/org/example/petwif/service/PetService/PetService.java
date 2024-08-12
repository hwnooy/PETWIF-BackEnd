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
@RequiredArgsConstructor
public class PetService {
    private final PetRepository petRepository;
    private final MemberRepository memberRepository;
    // id 매칭을 위한 accessToken -> member repository에서 id 가져오기

    public void addPet(List<PetRequestDto> dto){
        // Long memberid = memberRepository.find();

        for (PetRequestDto petRequestDto : dto){
            Pet pet = new Pet();
            pet.setPetAge(petRequestDto.getAge());
            pet.setPetGender(petRequestDto.getGender());
            pet.setPetName(petRequestDto.getPetName());
            pet.setPetKind(petRequestDto.getPetKind());
            petRepository.save(pet);
        }
    }
    public Pet editPet(Long id, List<PetRequestDto> dto){
        Member member = memberRepository.findByMemberId(id);

        return null;
        //pet.patch(pet);
    }

    //private void patch(Pet pet){
//        if (pet.getPetName()!=null) pet.set
//    }
    //public void  deletePet


}
