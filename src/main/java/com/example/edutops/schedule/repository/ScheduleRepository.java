package com.example.edutops.schedule.repository;

import com.example.edutops.common.repository.BaseRepository;
import com.example.edutops.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ScheduleRepository extends BaseRepository<Schedule> {

    List<Schedule> findByClassGroupPublicId(UUID classGroupPublicId);

    // Kiểm tra xung đột phòng học (khi tạo mới)
    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM Schedule s " +
           "WHERE s.room.id = :roomId AND s.dayOfWeek = :dayOfWeek " +
           "AND s.startTime < :endTime AND s.endTime > :startTime")
    boolean existsConflictingRoomSchedule(
            @Param("roomId") Long roomId,
            @Param("dayOfWeek") DayOfWeek dayOfWeek,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime);

    // Kiểm tra xung đột phòng học (khi cập nhật - loại trừ chính nó)
    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM Schedule s " +
           "WHERE s.room.id = :roomId AND s.dayOfWeek = :dayOfWeek " +
           "AND s.startTime < :endTime AND s.endTime > :startTime AND s.publicId <> :excludePublicId")
    boolean existsConflictingRoomScheduleExcludingSelf(
            @Param("roomId") Long roomId,
            @Param("dayOfWeek") DayOfWeek dayOfWeek,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime,
            @Param("excludePublicId") UUID excludePublicId);

    // Kiểm tra xung đột giáo viên (khi tạo mới)
    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM Schedule s " +
           "WHERE s.classGroup.teacher.id = :teacherId AND s.dayOfWeek = :dayOfWeek " +
           "AND s.startTime < :endTime AND s.endTime > :startTime")
    boolean existsConflictingTeacherSchedule(
            @Param("teacherId") Long teacherId,
            @Param("dayOfWeek") DayOfWeek dayOfWeek,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime);

    // Kiểm tra xung đột giáo viên (khi cập nhật - loại trừ chính nó)
    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM Schedule s " +
           "WHERE s.classGroup.teacher.id = :teacherId AND s.dayOfWeek = :dayOfWeek " +
           "AND s.startTime < :endTime AND s.endTime > :startTime AND s.publicId <> :excludePublicId")
    boolean existsConflictingTeacherSchedulesExcludingSelf(
            @Param("teacherId") Long teacherId,
            @Param("dayOfWeek") DayOfWeek dayOfWeek,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime,
            @Param("excludePublicId") UUID excludePublicId);
}
