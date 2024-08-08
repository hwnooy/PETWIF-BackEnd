package org.example.petwif.web.dto.PetDto;

import lombok.Getter;
import org.example.petwif.domain.enums.Gender;
import org.example.petwif.domain.enums.PetGender;

@Getter
public class PetRequestDto {
    private String petName;
    private PetGender gender;
    private int age;
    private String petKind;
}
