package org.example.petwif.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.example.petwif.validation.validator.FriendExistValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = FriendExistValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistFriend {
    String message() default "해당 친구 관계가 존재하지 않습니다";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
