package com.example.edutops.attendance.service;

import com.example.edutops.attendance.dto.AttendanceCreateRequest;
import com.example.edutops.attendance.dto.AttendanceUpdateRequest;
import com.example.edutops.attendance.dto.AttendanceResponse;
import com.example.edutops.attendance.dto.ClassAttendanceBatchRequest;
import com.example.edutops.common.service.BaseService;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface AttendanceService extends BaseService<AttendanceCreateRequest, AttendanceUpdateRequest, AttendanceResponse> {

    List<AttendanceResponse> getByStudent(UUID studentPublicId);

    List<AttendanceResponse> getByScheduleAndDate(UUID schedulePublicId, LocalDate date);

    List<AttendanceResponse> saveClassAttendanceBatch(ClassAttendanceBatchRequest request);
}
