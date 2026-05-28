package com.example.edutops.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator kiểm tra định dạng số điện thoại Việt Nam hợp lệ.
 */
public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {

    private static final String PHONE_REGEX = "^(0|\\+84)(3|5|7|8|9)[0-9]{8}$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // Để trống sẽ do các annotation khác như @NotBlank hoặc @NotNull xử lý nếu là bắt buộc
        if (value == null || value.trim().isEmpty()) {
            return true;
        }
        return value.matches(PHONE_REGEX);
    }
}
