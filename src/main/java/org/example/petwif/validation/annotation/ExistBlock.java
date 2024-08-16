package org.example.petwif.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.example.petwif.validation.validator.BlockExistValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = BlockExistValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistBlock {
    String message() default "해당 차단 내역이 존재하지 않습니다";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
