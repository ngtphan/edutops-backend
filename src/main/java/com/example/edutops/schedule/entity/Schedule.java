package com.example.edutops.schedule.entity;

import com.example.edutops.classgroup.entity.ClassGroup;
import com.example.edutops.common.entity.BaseEntity;
import com.example.edutops.room.entity.Room;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Lịch học cụ thể — lớp nào, phòng nào, thứ mấy, giờ nào.
 * <p>
 * Sử dụng {@code effectiveFrom}/{@code effectiveTo} cho lịch có thời hạn,
 * hỗ trợ thay đổi lịch giữa chừng.
 * </p>
 */
@Entity
@Table(name = "schedules")
public class Schedule extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_group_id", nullable = false)
    private ClassGroup classGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @NotNull
    @Column(nullable = false)
    private DayOfWeek dayOfWeek;

    @NotNull
    @Column(nullable = false)
    private LocalTime startTime;

    @NotNull
    @Column(nullable = false)
    private LocalTime endTime;

    /** Ngày bắt đầu áp dụng lịch này */
    private LocalDate effectiveFrom;

    /** Ngày kết thúc áp dụng lịch này (null = vô thời hạn) */
    private LocalDate effectiveTo;

    // --- Constructors ---

    public Schedule() {
    }

    public Schedule(ClassGroup classGroup, Room room, DayOfWeek dayOfWeek,
                    LocalTime startTime, LocalTime endTime) {
        this.classGroup = classGroup;
        this.room = room;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // --- Getters & Setters ---

    public ClassGroup getClassGroup() {
        return classGroup;
    }

    public void setClassGroup(ClassGroup classGroup) {
        this.classGroup = classGroup;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
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
