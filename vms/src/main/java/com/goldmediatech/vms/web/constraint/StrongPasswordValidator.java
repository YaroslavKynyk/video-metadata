package com.goldmediatech.vms.web.constraint;

public class StrongPasswordValidator implements jakarta.validation.ConstraintValidator<StrongPassword, String> {

    public static final String REGEXP_STRONG_PASSWORD = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#^()_+=-]).{8,}$";

    @Override
    public boolean isValid(String password, jakarta.validation.ConstraintValidatorContext context) {
        if (password == null) {
            return false;
        }
        return password.matches(REGEXP_STRONG_PASSWORD);
    }

}
