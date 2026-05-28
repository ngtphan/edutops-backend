package com.example.edutops.schedule.service.impl;

import com.example.edutops.classgroup.entity.ClassGroup;
import com.example.edutops.classgroup.repository.ClassGroupRepository;
import com.example.edutops.common.exception.BusinessException;
import com.example.edutops.common.exception.ErrorCode;
import com.example.edutops.common.mapper.EntityMapper;
import com.example.edutops.common.service.impl.BaseServiceImpl;
import com.example.edutops.room.entity.Room;
import com.example.edutops.room.repository.RoomRepository;
import com.example.edutops.schedule.dto.ScheduleCreateRequest;
import com.example.edutops.schedule.dto.ScheduleResponse;
import com.example.edutops.schedule.dto.ScheduleUpdateRequest;
import com.example.edutops.schedule.entity.Schedule;
import com.example.edutops.schedule.repository.ScheduleRepository;
import com.example.edutops.schedule.service.ScheduleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ScheduleServiceImpl
        extends BaseServiceImpl<ScheduleCreateRequest, ScheduleUpdateRequest, ScheduleResponse, Schedule>
        implements ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final ClassGroupRepository classGroupRepository;
    private final RoomRepository roomRepository;
    private final EntityMapper entityMapper;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository,
                               ClassGroupRepository classGroupRepository,
                               RoomRepository roomRepository,
                               EntityMapper entityMapper) {
        super(scheduleRepository);
        this.scheduleRepository = scheduleRepository;
        this.classGroupRepository = classGroupRepository;
        this.roomRepository = roomRepository;
        this.entityMapper = entityMapper;
    }

    @Override
    @Transactional
    public ScheduleResponse create(ScheduleCreateRequest request) {
        ClassGroup classGroup = classGroupRepository.findByPublicId(request.getClassGroupPublicId())
                .orElseThrow(() -> BusinessException.withDetail(ErrorCode.RESOURCE_NOT_FOUND, request.getClassGroupPublicId()));

        Room room = roomRepository.findByPublicId(request.getRoomPublicId())
                .orElseThrow(() -> BusinessException.withDetail(ErrorCode.RESOURCE_NOT_FOUND, request.getRoomPublicId()));

        validateTimeRange(request.getStartTime(), request.getEndTime());
        validateEffectiveDateRange(request.getEffectiveFrom(), request.getEffectiveTo());

        // 1. Kiểm tra trùng phòng học
        List<Schedule> roomConflicts = scheduleRepository.findConflictingRoomSchedules(
                room.getId(), request.getDayOfWeek(), request.getStartTime(), request.getEndTime());
        if (!roomConflicts.isEmpty()) {
            throw new BusinessException(ErrorCode.SCHEDULE_CONFLICT,
                    "Xung đột lịch: Phòng học '" + room.getName() + "' đã bận trong khung giờ này");
        }

        // 2. Kiểm tra trùng lịch giảng dạy của giáo viên
        List<Schedule> teacherConflicts = scheduleRepository.findConflictingTeacherSchedules(
                classGroup.getTeacher().getId(), request.getDayOfWeek(), request.getStartTime(), request.getEndTime());
        if (!teacherConflicts.isEmpty()) {
            String teacherName = resolveTeacherName(classGroup);
            throw new BusinessException(ErrorCode.SCHEDULE_CONFLICT,
                    "Xung đột lịch: Giáo viên '" + teacherName + "' đã bận trong khung giờ này");
        }

        Schedule schedule = new Schedule();
        schedule.setClassGroup(classGroup);
        schedule.setRoom(room);
        schedule.setDayOfWeek(request.getDayOfWeek());
        schedule.setStartTime(request.getStartTime());
        schedule.setEndTime(request.getEndTime());
        schedule.setEffectiveFrom(request.getEffectiveFrom());
        schedule.setEffectiveTo(request.getEffectiveTo());

        Schedule saved = scheduleRepository.save(schedule);
        return convertToResponse(saved);
    }

    @Override
    @Transactional
    public ScheduleResponse update(UUID publicId, ScheduleUpdateRequest request) {
        Schedule schedule = scheduleRepository.findByPublicId(publicId)
                .orElseThrow(() -> BusinessException.withDetail(ErrorCode.RESOURCE_NOT_FOUND, publicId));

        Room room = roomRepository.findByPublicId(request.getRoomPublicId())
                .orElseThrow(() -> BusinessException.withDetail(ErrorCode.RESOURCE_NOT_FOUND, request.getRoomPublicId()));

        validateTimeRange(request.getStartTime(), request.getEndTime());
        validateEffectiveDateRange(request.getEffectiveFrom(), request.getEffectiveTo());

        // 1. Kiểm tra trùng phòng học (loại trừ chính nó)
        List<Schedule> roomConflicts = scheduleRepository.findConflictingRoomSchedulesExcludingSelf(
                room.getId(), request.getDayOfWeek(), request.getStartTime(), request.getEndTime(), publicId);
        if (!roomConflicts.isEmpty()) {
            throw new BusinessException(ErrorCode.SCHEDULE_CONFLICT,
                    "Xung đột lịch: Phòng học '" + room.getName() + "' đã bận trong khung giờ này");
        }

        // 2. Kiểm tra trùng lịch giảng dạy của giáo viên (loại trừ chính nó)
        List<Schedule> teacherConflicts = scheduleRepository.findConflictingTeacherSchedulesExcludingSelf(
                schedule.getClassGroup().getTeacher().getId(), request.getDayOfWeek(), request.getStartTime(), request.getEndTime(), publicId);
        if (!teacherConflicts.isEmpty()) {
            String teacherName = resolveTeacherName(schedule.getClassGroup());
            throw new BusinessException(ErrorCode.SCHEDULE_CONFLICT,
                    "Xung đột lịch: Giáo viên '" + teacherName + "' đã bận trong khung giờ này");
        }

        schedule.setRoom(room);
        schedule.setDayOfWeek(request.getDayOfWeek());
        schedule.setStartTime(request.getStartTime());
        schedule.setEndTime(request.getEndTime());
        schedule.setEffectiveFrom(request.getEffectiveFrom());
        schedule.setEffectiveTo(request.getEffectiveTo());

        Schedule saved = scheduleRepository.save(schedule);
        return convertToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScheduleResponse> getByClassGroup(UUID classGroupPublicId) {
        return scheduleRepository.findByClassGroupPublicId(classGroupPublicId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    protected Schedule convertToEntity(ScheduleCreateRequest request) {
        throw new UnsupportedOperationException("Sử dụng luồng nghiệp vụ custom trong hàm create()");
    }

    @Override
    protected ScheduleResponse convertToResponse(Schedule entity) {
        ScheduleResponse response = new ScheduleResponse();
        response.setPublicId(entity.getPublicId());
        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedAt(entity.getUpdatedAt());
        response.setDayOfWeek(entity.getDayOfWeek());
        response.setStartTime(entity.getStartTime());
        response.setEndTime(entity.getEndTime());
        response.setEffectiveFrom(entity.getEffectiveFrom());
        response.setEffectiveTo(entity.getEffectiveTo());
        response.setClassGroup(entityMapper.toClassGroupResponse(entity.getClassGroup()));
        response.setRoom(entityMapper.toRoomResponse(entity.getRoom()));
        return response;
    }

    @Override
    protected void updateEntityFromRequest(Schedule entity, ScheduleUpdateRequest request) {
        throw new UnsupportedOperationException("Sử dụng luồng nghiệp vụ custom trong hàm update()");
    }

    // ========== Private Helper Methods ==========

    /**
     * Kiểm tra giờ bắt đầu phải trước giờ kết thúc.
     */
    private void validateTimeRange(java.time.LocalTime startTime, java.time.LocalTime endTime) {
        if (startTime.isAfter(endTime) || startTime.equals(endTime)) {
            throw new BusinessException(ErrorCode.VALIDATION_FAILED, "Giờ bắt đầu phải trước giờ kết thúc");
        }
    }

    /**
     * Kiểm tra ngày áp dụng lịch học hợp lệ.
     */
    private void validateEffectiveDateRange(java.time.LocalDate effectiveFrom, java.time.LocalDate effectiveTo) {
        if (effectiveTo != null && effectiveFrom != null && effectiveFrom.isAfter(effectiveTo)) {
            throw new BusinessException(ErrorCode.VALIDATION_FAILED, "Ngày áp dụng lịch học không hợp lệ");
        }
    }

    /**
     * Lấy tên giáo viên an toàn (null-safe).
     */
    private String resolveTeacherName(ClassGroup classGroup) {
        if (classGroup.getTeacher() != null && classGroup.getTeacher().getUser() != null) {
            return classGroup.getTeacher().getUser().getFullName();
        }
        return "Giáo viên";
    }
}
