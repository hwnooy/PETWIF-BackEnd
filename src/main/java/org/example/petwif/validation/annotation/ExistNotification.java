package org.example.petwif.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.example.petwif.validation.validator.MemberExistsValidator;
import org.example.petwif.validation.validator.NotificationExistValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NotificationExistValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistNotification {
    String message() default "해당 알람이 존재하지 않습니다";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
