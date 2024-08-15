package org.example.petwif.web.dto.PetDto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.petwif.domain.enums.Gender;
import org.example.petwif.domain.enums.PetGender;

@Getter
@Builder
public class PetRequestDto {
    private String petName;

    @Enumerated(EnumType.STRING)
    private String gender;

    private int age;
    private String petKind;
}
