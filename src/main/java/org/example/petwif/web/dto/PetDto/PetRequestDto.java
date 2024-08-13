package org.example.petwif.web.dto.PetDto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.petwif.domain.enums.Gender;
import org.example.petwif.domain.enums.PetGender;

@Getter
@Builder
public class PetRequestDto {
    private String petName;
    private PetGender gender;
    private int age;
    private String petKind;
}
