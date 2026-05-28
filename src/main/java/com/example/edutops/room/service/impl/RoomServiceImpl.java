package com.example.edutops.room.service.impl;

import com.example.edutops.common.exception.BusinessException;
import com.example.edutops.common.exception.ErrorCode;
import com.example.edutops.common.mapper.EntityMapper;
import com.example.edutops.common.service.impl.BaseServiceImpl;
import com.example.edutops.room.dto.RoomCreateRequest;
import com.example.edutops.room.dto.RoomResponse;
import com.example.edutops.room.dto.RoomUpdateRequest;
import com.example.edutops.room.entity.Room;
import com.example.edutops.room.repository.RoomRepository;
import com.example.edutops.room.service.RoomService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class RoomServiceImpl
        extends BaseServiceImpl<RoomCreateRequest, RoomUpdateRequest, RoomResponse, Room>
        implements RoomService {

    private final RoomRepository roomRepository;
    private final EntityMapper entityMapper;

    public RoomServiceImpl(RoomRepository roomRepository, EntityMapper entityMapper) {
        super(roomRepository);
        this.roomRepository = roomRepository;
        this.entityMapper = entityMapper;
    }

    @Override
    @Transactional
    public RoomResponse create(RoomCreateRequest request) {
        if (roomRepository.existsByName(request.getName())) {
            throw BusinessException.withDetail(ErrorCode.ROOM_NAME_ALREADY_EXISTS, request.getName());
        }
        return super.create(request);
    }

    @Override
    @Transactional
    public RoomResponse update(UUID publicId, RoomUpdateRequest request) {
        Room room = roomRepository.findByPublicId(publicId)
                .orElseThrow(() -> BusinessException.withDetail(ErrorCode.RESOURCE_NOT_FOUND, publicId));

        if (!room.getName().equalsIgnoreCase(request.getName())
                && roomRepository.existsByNameAndPublicIdNot(request.getName(), publicId)) {
            throw BusinessException.withDetail(ErrorCode.ROOM_NAME_ALREADY_EXISTS, request.getName());
        }

        room.setName(request.getName());
        room.setCapacity(request.getCapacity());
        room.setLocation(request.getLocation());
        room.setAvailable(request.isAvailable());

        Room saved = roomRepository.save(room);
        return convertToResponse(saved);
    }

    @Override
    protected Room convertToEntity(RoomCreateRequest request) {
        Room room = new Room();
        room.setName(request.getName());
        room.setCapacity(request.getCapacity());
        room.setLocation(request.getLocation());
        room.setAvailable(true);
        return room;
    }

    @Override
    protected RoomResponse convertToResponse(Room entity) {
        return entityMapper.toRoomResponse(entity);
    }

    @Override
    protected void updateEntityFromRequest(Room entity, RoomUpdateRequest request) {
        entity.setName(request.getName());
        entity.setCapacity(request.getCapacity());
        entity.setLocation(request.getLocation());
        entity.setAvailable(request.isAvailable());
    }
}
