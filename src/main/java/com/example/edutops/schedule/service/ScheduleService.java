package com.example.edutops.schedule.service;

import com.example.edutops.common.service.BaseService;
import com.example.edutops.schedule.dto.ScheduleCreateRequest;
import com.example.edutops.schedule.dto.ScheduleUpdateRequest;
import com.example.edutops.schedule.dto.ScheduleResponse;

import java.util.List;
import java.util.UUID;

public interface ScheduleService extends BaseService<ScheduleCreateRequest, ScheduleUpdateRequest, ScheduleResponse> {

    List<ScheduleResponse> getByClassGroup(UUID classGroupPublicId);
}
