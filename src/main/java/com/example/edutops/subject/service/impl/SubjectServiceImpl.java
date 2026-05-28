package com.example.edutops.subject.service.impl;

import com.example.edutops.common.exception.BusinessException;
import com.example.edutops.common.exception.ErrorCode;
import com.example.edutops.common.service.impl.BaseServiceImpl;
import com.example.edutops.subject.dto.SubjectRequest;
import com.example.edutops.subject.dto.SubjectResponse;
import com.example.edutops.subject.entity.Subject;
import com.example.edutops.subject.repository.SubjectRepository;
import com.example.edutops.subject.service.SubjectService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class SubjectServiceImpl 
        extends BaseServiceImpl<SubjectRequest, SubjectRequest, SubjectResponse, Subject> 
        implements SubjectService {

    private final SubjectRepository subjectRepository;

    public SubjectServiceImpl(SubjectRepository subjectRepository) {
        super(subjectRepository);
        this.subjectRepository = subjectRepository;
    }

    @Override
    @Transactional
    public SubjectResponse create(SubjectRequest request) {
        if (subjectRepository.existsByCode(request.getCode())) {
            throw new BusinessException(ErrorCode.SUBJECT_CODE_ALREADY_EXISTS, 
                    "Mã môn học '" + request.getCode() + "' đã tồn tại trong hệ thống");
        }
        return super.create(request);
    }

    @Override
    @Transactional
    public SubjectResponse update(UUID publicId, SubjectRequest request) {
        Subject entity = subjectRepository.findByPublicId(publicId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, 
                        "Không tìm thấy môn học với ID: " + publicId));
        
        // Nếu thay đổi code, cần kiểm tra trùng lặp
        if (!entity.getCode().equalsIgnoreCase(request.getCode()) 
                && subjectRepository.existsByCode(request.getCode())) {
            throw new BusinessException(ErrorCode.SUBJECT_CODE_ALREADY_EXISTS, 
                    "Mã môn học '" + request.getCode() + "' đã tồn tại trong hệ thống");
        }

        updateEntityFromRequest(entity, request);
        Subject saved = subjectRepository.save(entity);
        return convertToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public SubjectResponse getByCode(String code) {
        Subject entity = subjectRepository.findByCode(code)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, 
                        "Không tìm thấy môn học với mã: " + code));
        return convertToResponse(entity);
    }

    @Override
    protected Subject convertToEntity(SubjectRequest request) {
        Subject subject = new Subject();
        subject.setCode(request.getCode());
        subject.setName(request.getName());
        subject.setDescription(request.getDescription());
        return subject;
    }

    @Override
    protected SubjectResponse convertToResponse(Subject entity) {
        SubjectResponse response = new SubjectResponse();
        response.setPublicId(entity.getPublicId());
        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedAt(entity.getUpdatedAt());
        response.setCode(entity.getCode());
        response.setName(entity.getName());
        response.setDescription(entity.getDescription());
        return response;
    }

    @Override
    protected void updateEntityFromRequest(Subject entity, SubjectRequest request) {
        entity.setCode(request.getCode());
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
    }
}
