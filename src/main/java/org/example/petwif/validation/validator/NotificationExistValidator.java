package org.example.petwif.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.example.petwif.apiPayload.code.status.ErrorStatus;
import org.example.petwif.domain.entity.Notification;
import org.example.petwif.service.NotificationService.NotificationQueryService;
import org.example.petwif.validation.annotation.ExistNotification;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class NotificationExistValidator implements ConstraintValidator<ExistNotification, Long> {

    private final NotificationQueryService notificationQueryService;

    @Override
    public void initialize(ExistNotification constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        Optional<Notification> target = notificationQueryService.findNotification(value);

        if(target.isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ErrorStatus.NOTIFICATION_NOT_FOUND.toString()).addConstraintViolation();
            return false;
        }
        return true;
    }
}
