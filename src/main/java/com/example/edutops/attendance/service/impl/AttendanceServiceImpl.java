package com.example.edutops.attendance.service.impl;

import com.example.edutops.attendance.dto.AttendanceCreateRequest;
import com.example.edutops.attendance.dto.AttendanceResponse;
import com.example.edutops.attendance.dto.AttendanceUpdateRequest;
import com.example.edutops.attendance.dto.ClassAttendanceBatchRequest;
import com.example.edutops.attendance.dto.StudentAttendanceDto;
import com.example.edutops.attendance.entity.Attendance;
import com.example.edutops.attendance.repository.AttendanceRepository;
import com.example.edutops.attendance.service.AttendanceService;
import com.example.edutops.common.exception.BusinessException;
import com.example.edutops.common.exception.ErrorCode;
import com.example.edutops.common.mapper.EntityMapper;
import com.example.edutops.common.service.impl.BaseServiceImpl;
import com.example.edutops.enrollment.repository.EnrollmentRepository;
import com.example.edutops.schedule.entity.Schedule;
import com.example.edutops.schedule.repository.ScheduleRepository;
import com.example.edutops.student.entity.Student;
import com.example.edutops.student.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AttendanceServiceImpl
        extends BaseServiceImpl<AttendanceCreateRequest, AttendanceUpdateRequest, AttendanceResponse, Attendance>
        implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final ScheduleRepository scheduleRepository;
    private final StudentRepository studentRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final EntityMapper entityMapper;

    public AttendanceServiceImpl(AttendanceRepository attendanceRepository,
                                 ScheduleRepository scheduleRepository,
                                 StudentRepository studentRepository,
                                 EnrollmentRepository enrollmentRepository,
                                 EntityMapper entityMapper) {
        super(attendanceRepository);
        this.attendanceRepository = attendanceRepository;
        this.scheduleRepository = scheduleRepository;
        this.studentRepository = studentRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.entityMapper = entityMapper;
    }

    @Override
    @Transactional
    public AttendanceResponse create(AttendanceCreateRequest request) {
        Schedule schedule = scheduleRepository.findByPublicId(request.getSchedulePublicId())
                .orElseThrow(() -> BusinessException.withDetail(ErrorCode.RESOURCE_NOT_FOUND, request.getSchedulePublicId()));

        Student student = studentRepository.findByPublicId(request.getStudentPublicId())
                .orElseThrow(() -> BusinessException.withDetail(ErrorCode.RESOURCE_NOT_FOUND, request.getStudentPublicId()));

        // 1. Kiểm tra học viên có thuộc lớp học của lịch này hay không
        if (!enrollmentRepository.existsByStudentIdAndClassGroupId(student.getId(), schedule.getClassGroup().getId())) {
            throw new BusinessException(ErrorCode.STUDENT_NOT_ENROLLED);
        }

        // 2. Kiểm tra trùng lặp điểm danh cho ngày hôm đó
        if (attendanceRepository.existsByScheduleIdAndStudentIdAndAttendanceDate(
                schedule.getId(), student.getId(), request.getAttendanceDate())) {
            throw new BusinessException(ErrorCode.ATTENDANCE_ALREADY_EXISTS);
        }

        Attendance attendance = new Attendance();
        attendance.setSchedule(schedule);
        attendance.setStudent(student);
        attendance.setAttendanceDate(request.getAttendanceDate());
        attendance.setStatus(request.getStatus());
        attendance.setNote(request.getNote());

        Attendance saved = attendanceRepository.save(attendance);
        return convertToResponse(saved);
    }

    @Override
    @Transactional
    public AttendanceResponse update(UUID publicId, AttendanceUpdateRequest request) {
        Attendance attendance = attendanceRepository.findByPublicId(publicId)
                .orElseThrow(() -> BusinessException.withDetail(ErrorCode.RESOURCE_NOT_FOUND, publicId));

        attendance.setStatus(request.getStatus());
        attendance.setNote(request.getNote());

        Attendance saved = attendanceRepository.save(attendance);
        return convertToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttendanceResponse> getByStudent(UUID studentPublicId) {
        return attendanceRepository.findByStudentPublicId(studentPublicId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttendanceResponse> getByScheduleAndDate(UUID schedulePublicId, LocalDate date) {
        return attendanceRepository.findBySchedulePublicIdAndAttendanceDate(schedulePublicId, date).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    protected Attendance convertToEntity(AttendanceCreateRequest request) {
        throw new UnsupportedOperationException("Sử dụng luồng nghiệp vụ custom trong hàm create()");
    }

    @Override
    protected AttendanceResponse convertToResponse(Attendance entity) {
        AttendanceResponse response = new AttendanceResponse();
        response.setPublicId(entity.getPublicId());
        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedAt(entity.getUpdatedAt());
        response.setAttendanceDate(entity.getAttendanceDate());
        response.setStatus(entity.getStatus());
        response.setNote(entity.getNote());
        response.setSchedule(entityMapper.toScheduleResponse(entity.getSchedule()));
        response.setStudent(entityMapper.toStudentResponse(entity.getStudent()));
        return response;
    }

    @Override
    protected void updateEntityFromRequest(Attendance entity, AttendanceUpdateRequest request) {
        throw new UnsupportedOperationException("Sử dụng luồng nghiệp vụ custom trong hàm update()");
    }

    @Override
    @Transactional
    public List<AttendanceResponse> saveClassAttendanceBatch(ClassAttendanceBatchRequest request) {
        Schedule schedule = scheduleRepository.findByPublicId(request.getSchedulePublicId())
                .orElseThrow(() -> BusinessException.withDetail(ErrorCode.RESOURCE_NOT_FOUND, request.getSchedulePublicId()));

        return request.getStudentAttendances().stream().map(dto -> {
            Student student = studentRepository.findByPublicId(dto.getStudentPublicId())
                    .orElseThrow(() -> BusinessException.withDetail(ErrorCode.RESOURCE_NOT_FOUND, dto.getStudentPublicId()));

            // 1. Kiểm tra học viên có đăng ký lớp học này hay không
            if (!enrollmentRepository.existsByStudentIdAndClassGroupId(student.getId(), schedule.getClassGroup().getId())) {
                throw new BusinessException(ErrorCode.STUDENT_NOT_ENROLLED,
                        "Học viên '" + student.getFullName() + "' không thuộc lớp học này");
            }

            // 2. Logic Upsert (Cập nhật nếu đã tồn tại, tạo mới nếu chưa)
            Attendance attendance = attendanceRepository
                    .findByScheduleIdAndStudentIdAndAttendanceDate(schedule.getId(), student.getId(), request.getAttendanceDate())
                    .orElseGet(() -> {
                        Attendance newAttendance = new Attendance();
                        newAttendance.setSchedule(schedule);
                        newAttendance.setStudent(student);
                        newAttendance.setAttendanceDate(request.getAttendanceDate());
                        return newAttendance;
                    });

            attendance.setStatus(dto.getStatus());
            attendance.setNote(dto.getNote());

            Attendance saved = attendanceRepository.save(attendance);
            return convertToResponse(saved);
        }).collect(Collectors.toList());
    }
}
