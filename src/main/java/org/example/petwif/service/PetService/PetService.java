package org.example.petwif.service.PetService;

import lombok.RequiredArgsConstructor;
import org.example.petwif.domain.entity.Member;
import org.example.petwif.domain.entity.Pet;
import org.example.petwif.domain.enums.Gender;
import org.example.petwif.domain.enums.PetGender;
import org.example.petwif.domain.enums.Telecom;
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

    public void addPet(Long id, List<PetRequestDto> dto){
        Member member = memberRepository.findByMemberId(id);
        for (PetRequestDto petRequestDto : dto){
            Pet pet = new Pet();
            pet.setPetAge(petRequestDto.getAge());
            pet.setPetGender(PetGender.valueOf(petRequestDto.getGender()));
            pet.setPetName(petRequestDto.getPetName());
            pet.setPetKind(petRequestDto.getPetKind());
            pet.setMember(member);
            petRepository.save(pet);

        }
    }


/*
        public void editPet(Long id, List<PetRequestDto> dto){
        Member member = memberRepository.findByMemberId(id);
//        private String petName;
//        private PetGender gender;
//        private int age;
//        private String petKind;

        for (PetRequestDto petRequestDto : dto){
            if (petRequestDto.getPetName() != null) {

            }
        }


        //pet.patch(pet);
    }
*/

}
