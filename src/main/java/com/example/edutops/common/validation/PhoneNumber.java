package com.example.edutops.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation validation số điện thoại Việt Nam hợp lệ.
 */
@Documented
@Constraint(validatedBy = PhoneNumberValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PhoneNumber {

    String message() default "Số điện thoại không hợp lệ (phải gồm 10 chữ số và bắt đầu bằng số 0 hoặc +84)";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
