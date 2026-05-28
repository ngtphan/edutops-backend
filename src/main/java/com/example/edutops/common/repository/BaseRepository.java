package com.example.edutops.common.repository;

import com.example.edutops.common.entity.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository chung hỗ trợ Dual ID Pattern.
 *
 * @param <T> Kiểu Entity kế thừa từ BaseEntity
 */
@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity> extends JpaRepository<T, Long> {
    
    /**
     * Tìm kiếm entity theo public ID (UUID).
     *
     * @param publicId ID công khai dạng UUID
     * @return Optional chứa Entity nếu tìm thấy
     */
    Optional<T> findByPublicId(UUID publicId);

    /**
     * Kiểm tra xem entity có tồn tại với public ID hay không.
     *
     * @param publicId ID công khai dạng UUID
     * @return true nếu tồn tại, ngược lại false
     */
    boolean existsByPublicId(UUID publicId);

    /**
     * Xóa entity theo public ID.
     *
     * @param publicId ID công khai dạng UUID
     */
    void deleteByPublicId(UUID publicId);
}
