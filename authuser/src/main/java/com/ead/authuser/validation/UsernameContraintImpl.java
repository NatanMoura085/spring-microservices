package com.ead.authuser.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UsernameContraintImpl implements ConstraintValidator<UsernameContraint,String> {
    @Override
    public void initialize(UsernameContraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext) {
        if (username == null || username.trim().isEmpty() || username.contains(" ")) {
            return false;

        }
        return true;
    }
}