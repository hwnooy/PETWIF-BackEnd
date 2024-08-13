package org.example.petwif.web.dto.PetDto;

import lombok.Builder;
import org.example.petwif.domain.enums.PetGender;

@Builder
public class PetResponseDto {
    private String petName;
    private PetGender gender;
    private int age;
    private String petKind;

}
