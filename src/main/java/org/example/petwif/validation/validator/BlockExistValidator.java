package org.example.petwif.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.example.petwif.apiPayload.code.status.ErrorStatus;
import org.example.petwif.domain.entity.Block;
import org.example.petwif.service.BlockService.BlockQueryService;
import org.example.petwif.validation.annotation.ExistBlock;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BlockExistValidator implements ConstraintValidator<ExistBlock, Long> {

    private final BlockQueryService blockQueryService;

    @Override
    public void initialize(ExistBlock constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        Optional<Block> target = blockQueryService.findBlock(value);

        if(target.isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ErrorStatus.BLOCK_NOT_FOUND.toString()).addConstraintViolation();
            return false;
        }
        return true;
    }
}
