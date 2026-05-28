package com.example.edutops.schedule.dto;

import jakarta.validation.constraints.NotNull;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public class ScheduleCreateRequest {

    @NotNull(message = "Lớp học không được để trống")
    private UUID classGroupPublicId;

    @NotNull(message = "Phòng học không được để trống")
    private UUID roomPublicId;

    @NotNull(message = "Thứ trong tuần không được để trống")
    private DayOfWeek dayOfWeek;

    @NotNull(message = "Giờ bắt đầu không được để trống")
    private LocalTime startTime;

    @NotNull(message = "Giờ kết thúc không được để trống")
    private LocalTime endTime;

    private LocalDate effectiveFrom;

    private LocalDate effectiveTo;

    // --- Getters & Setters ---

    public UUID getClassGroupPublicId() {
        return classGroupPublicId;
    }

    public void setClassGroupPublicId(UUID classGroupPublicId) {
        this.classGroupPublicId = classGroupPublicId;
    }

    public UUID getRoomPublicId() {
        return roomPublicId;
    }

    public void setRoomPublicId(UUID roomPublicId) {
        this.roomPublicId = roomPublicId;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public LocalDate getEffectiveFrom() {
        return effectiveFrom;
    }

    public void setEffectiveFrom(LocalDate effectiveFrom) {
        this.effectiveFrom = effectiveFrom;
    }

    public LocalDate getEffectiveTo() {
        return effectiveTo;
    }

    public void setEffectiveTo(LocalDate effectiveTo) {
        this.effectiveTo = effectiveTo;
    }
}
