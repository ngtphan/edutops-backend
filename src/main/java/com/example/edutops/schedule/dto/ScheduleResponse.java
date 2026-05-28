package com.example.edutops.schedule.dto;

import com.example.edutops.classgroup.dto.ClassGroupResponse;
import com.example.edutops.common.dto.BaseResponse;
import com.example.edutops.room.dto.RoomResponse;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

public class ScheduleResponse extends BaseResponse {

    private ClassGroupResponse classGroup;
    private RoomResponse room;
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;

    // --- Getters & Setters ---

    public ClassGroupResponse getClassGroup() {
        return classGroup;
    }

    public void setClassGroup(ClassGroupResponse classGroup) {
        this.classGroup = classGroup;
    }

    public RoomResponse getRoom() {
        return room;
    }

    public void setRoom(RoomResponse room) {
        this.room = room;
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
