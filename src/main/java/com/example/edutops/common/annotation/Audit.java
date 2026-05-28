package com.example.edutops.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Chú thích tùy chỉnh để tự động ghi log audit cho phương thức nghiệp vụ.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Audit {
    
    /**
     * Tên hành động thực hiện. Ví dụ: "CREATE_SUBJECT", "UPDATE_STUDENT", "DELETE_SCHEDULE".
     */
    String action();

    /**
     * Tên thực thể chịu tác động. Ví dụ: "Subject", "Student", "Schedule".
     */
    String entity();
}
