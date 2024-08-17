package org.example.petwif.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.example.petwif.apiPayload.code.status.ErrorStatus;
import org.example.petwif.domain.entity.Friend;
import org.example.petwif.service.FriendService.FriendQueryService;
import org.example.petwif.validation.annotation.ExistFriend;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FriendExistValidator implements ConstraintValidator<ExistFriend, Long> {

    private final FriendQueryService friendQueryService;

    @Override
    public void initialize(ExistFriend constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        Optional<Friend> target = friendQueryService.findFriend(value);

        if(target.isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ErrorStatus.FRIEND_NOT_FOUND.toString()).addConstraintViolation();
            return false;
        }
        return true;
    }
}
