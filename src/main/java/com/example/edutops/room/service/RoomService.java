package com.example.edutops.room.service;

import com.example.edutops.common.service.BaseService;
import com.example.edutops.room.dto.RoomCreateRequest;
import com.example.edutops.room.dto.RoomUpdateRequest;
import com.example.edutops.room.dto.RoomResponse;

public interface RoomService extends BaseService<RoomCreateRequest, RoomUpdateRequest, RoomResponse> {
}
