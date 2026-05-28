package com.example.edutops.common.service;

import java.util.List;
import java.util.UUID;

/**
 * Interface Generic CRUD Service.
 *
 * @param <CRE> DTO Request dùng cho việc tạo mới
 * @param <UPD> DTO Request dùng cho việc cập nhật
 * @param <RES> DTO Response trả về cho client
 */
public interface BaseService<CRE, UPD, RES> {

    /**
     * Tạo mới một bản ghi.
     *
     * @param createRequest Thông tin bản ghi mới
     * @return Bản ghi đã được lưu dưới dạng Response DTO
     */
    RES create(CRE createRequest);

    /**
     * Cập nhật thông tin bản ghi hiện có theo public ID (UUID).
     *
     * @param publicId ID công khai dạng UUID
     * @param updateRequest Thông tin cần cập nhật
     * @return Bản ghi đã cập nhật dưới dạng Response DTO
     */
    RES update(UUID publicId, UPD updateRequest);

    /**
     * Lấy thông tin chi tiết một bản ghi theo public ID.
     *
     * @param publicId ID công khai dạng UUID
     * @return Bản ghi chi tiết dưới dạng Response DTO
     */
    RES getByPublicId(UUID publicId);

    /**
     * Lấy danh sách toàn bộ bản ghi.
     *
     * @return Danh sách Response DTO
     */
    List<RES> getAll();

    /**
     * Xóa bản ghi theo public ID.
     *
     * @param publicId ID công khai dạng UUID
     */
    void delete(UUID publicId);
}
