package com.example.edutops.course.entity;

/**
 * Trạng thái khóa học.
 */
public enum CourseStatus {

    /** Đang hoạt động, có thể mở lớp mới */
    ACTIVE,

    /** Tạm ngừng, không mở lớp mới */
    INACTIVE,

    /** Đã lưu trữ */
    ARCHIVED
}
