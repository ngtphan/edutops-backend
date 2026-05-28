package com.example.edutops.attendance.repository;

import com.example.edutops.attendance.entity.Attendance;
import com.example.edutops.common.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface AttendanceRepository extends BaseRepository<Attendance> {

    boolean existsByScheduleIdAndStudentIdAndAttendanceDate(Long scheduleId, Long studentId, LocalDate attendanceDate);

    java.util.Optional<Attendance> findByScheduleIdAndStudentIdAndAttendanceDate(Long scheduleId, Long studentId, LocalDate attendanceDate);

    List<Attendance> findByStudentPublicId(UUID studentPublicId);

    @Query("SELECT a FROM Attendance a WHERE a.schedule.publicId = :schedulePublicId AND a.attendanceDate = :date")
    List<Attendance> findBySchedulePublicIdAndAttendanceDate(
            @Param("schedulePublicId") UUID schedulePublicId,
            @Param("date") LocalDate date);
}
