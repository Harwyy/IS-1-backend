package com.is.lw.core.validator;

import com.is.lw.core.validator.annotation.ValidEnum;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EnumValidator implements ConstraintValidator<ValidEnum, String> {

    private Enum<?>[] enumValues;

    @Override
    public void initialize(ValidEnum annotation) {
        enumValues = annotation.enumClass().getEnumConstants();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        for (Enum<?> e : enumValues) {
            if (e.name().equals(value)) {
                return true;
            }
        }
        return false;
    }
}
