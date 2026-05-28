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

        // 1. Kiểm tra trùng phòng học
        if (scheduleRepository.existsConflictingRoomSchedule(
                room.getId(), request.getDayOfWeek(), request.getStartTime(), request.getEndTime())) {
            throw BusinessException.withDetail(ErrorCode.SCHEDULE_CONFLICT, "Phòng học: " + room.getName());
        }

        // 2. Kiểm tra trùng lịch giảng dạy của giáo viên
        if (scheduleRepository.existsConflictingTeacherSchedule(
                classGroup.getTeacher().getId(), request.getDayOfWeek(), request.getStartTime(), request.getEndTime())) {
            String teacherName = classGroup.getTeacherName();
            throw BusinessException.withDetail(ErrorCode.SCHEDULE_CONFLICT, "Giáo viên: " + teacherName);
        }

        Schedule schedule = new Schedule();
        schedule.setClassGroup(classGroup);
        schedule.setRoom(room);
        schedule.setDayOfWeek(request.getDayOfWeek());
        schedule.setStartTime(request.getStartTime());
        schedule.setEndTime(request.getEndTime());
        schedule.setEffectiveFrom(request.getEffectiveFrom());
        schedule.setEffectiveTo(request.getEffectiveTo());

        schedule.validate(); // Tự động kiểm tra ràng buộc bằng OOP!

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

        // 1. Kiểm tra trùng phòng học (loại trừ chính nó)
        if (scheduleRepository.existsConflictingRoomScheduleExcludingSelf(
                room.getId(), request.getDayOfWeek(), request.getStartTime(), request.getEndTime(), publicId)) {
            throw BusinessException.withDetail(ErrorCode.SCHEDULE_CONFLICT, "Phòng học: " + room.getName());
        }

        // 2. Kiểm tra trùng lịch giảng dạy của giáo viên (loại trừ chính nó)
        if (scheduleRepository.existsConflictingTeacherSchedulesExcludingSelf(
                schedule.getClassGroup().getTeacher().getId(), request.getDayOfWeek(), request.getStartTime(), request.getEndTime(), publicId)) {
            String teacherName = schedule.getClassGroup().getTeacherName();
            throw BusinessException.withDetail(ErrorCode.SCHEDULE_CONFLICT, "Giáo viên: " + teacherName);
        }

        schedule.setRoom(room);
        schedule.setDayOfWeek(request.getDayOfWeek());
        schedule.setStartTime(request.getStartTime());
        schedule.setEndTime(request.getEndTime());
        schedule.setEffectiveFrom(request.getEffectiveFrom());
        schedule.setEffectiveTo(request.getEffectiveTo());

        schedule.validate(); // Tự động kiểm tra ràng buộc bằng OOP!

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
    }}
