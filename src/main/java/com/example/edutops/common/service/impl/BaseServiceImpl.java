package com.example.edutops.common.service.impl;

import com.example.edutops.common.entity.BaseEntity;
import com.example.edutops.common.exception.BusinessException;
import com.example.edutops.common.exception.ErrorCode;
import com.example.edutops.common.repository.BaseRepository;
import com.example.edutops.common.service.BaseService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Lớp trừu tượng triển khai các thao tác CRUD cơ bản sử dụng Template Method Pattern.
 *
 * @param <CRE> DTO Request dùng cho việc tạo mới
 * @param <UPD> DTO Request dùng cho việc cập nhật
 * @param <RES> DTO Response trả về cho client
 * @param <T>   Kiểu Entity tương ứng, kế thừa từ BaseEntity
 */
public abstract class BaseServiceImpl<CRE, UPD, RES, T extends BaseEntity> 
        implements BaseService<CRE, UPD, RES> {

    protected final BaseRepository<T> repository;

    protected BaseServiceImpl(BaseRepository<T> repository) {
        this.repository = repository;
    }

    /**
     * Chuyển đổi từ Create Request DTO sang Entity.
     */
    protected abstract T convertToEntity(CRE request);

    /**
     * Chuyển đổi từ Entity sang Response DTO.
     */
    protected abstract RES convertToResponse(T entity);

    /**
     * Cập nhật các giá trị của Entity từ Update Request DTO.
     */
    protected abstract void updateEntityFromRequest(T entity, UPD request);

    @Override
    @Transactional
    public RES create(CRE request) {
        T entity = convertToEntity(request);
        T saved = repository.save(entity);
        return convertToResponse(saved);
    }

    @Override
    @Transactional
    public RES update(UUID publicId, UPD request) {
        T entity = repository.findByPublicId(publicId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, 
                        "Không tìm thấy tài nguyên yêu cầu với ID: " + publicId));
        updateEntityFromRequest(entity, request);
        T saved = repository.save(entity);
        return convertToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public RES getByPublicId(UUID publicId) {
        T entity = repository.findByPublicId(publicId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, 
                        "Không tìm thấy tài nguyên yêu cầu với ID: " + publicId));
        return convertToResponse(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RES> getAll() {
        return repository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(UUID publicId) {
        T entity = repository.findByPublicId(publicId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, 
                        "Không tìm thấy tài nguyên yêu cầu với ID: " + publicId));
        repository.delete(entity);
    }
}
