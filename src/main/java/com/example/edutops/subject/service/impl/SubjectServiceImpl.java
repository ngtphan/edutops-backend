package com.example.edutops.subject.service.impl;

import com.example.edutops.common.exception.BusinessException;
import com.example.edutops.common.exception.ErrorCode;
import com.example.edutops.common.mapper.EntityMapper;
import com.example.edutops.common.service.impl.BaseServiceImpl;
import com.example.edutops.subject.dto.SubjectRequest;
import com.example.edutops.subject.dto.SubjectResponse;
import com.example.edutops.subject.entity.Subject;
import com.example.edutops.subject.repository.SubjectRepository;
import com.example.edutops.subject.service.SubjectService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.edutops.common.annotation.Audit;

import java.util.UUID;

@Service
public class SubjectServiceImpl 
        extends BaseServiceImpl<SubjectRequest, SubjectRequest, SubjectResponse, Subject> 
        implements SubjectService {

    private final SubjectRepository subjectRepository;
    private final EntityMapper entityMapper;

    public SubjectServiceImpl(SubjectRepository subjectRepository, EntityMapper entityMapper) {
        super(subjectRepository);
        this.subjectRepository = subjectRepository;
        this.entityMapper = entityMapper;
    }

    @Override
    @Transactional
    @Audit(action = "CREATE_SUBJECT", entity = "Subject")
    public SubjectResponse create(SubjectRequest request) {
        if (subjectRepository.existsByCode(request.getCode())) {
            throw BusinessException.withDetail(ErrorCode.SUBJECT_CODE_ALREADY_EXISTS, request.getCode());
        }
        return super.create(request);
    }

    @Override
    @Transactional
    @Audit(action = "UPDATE_SUBJECT", entity = "Subject")
    public SubjectResponse update(UUID publicId, SubjectRequest request) {
        Subject entity = subjectRepository.findByPublicId(publicId)
                .orElseThrow(() -> BusinessException.withDetail(ErrorCode.RESOURCE_NOT_FOUND, publicId));
        
        // Nếu thay đổi code, cần kiểm tra trùng lặp
        if (!entity.getCode().equalsIgnoreCase(request.getCode()) 
                && subjectRepository.existsByCode(request.getCode())) {
            throw BusinessException.withDetail(ErrorCode.SUBJECT_CODE_ALREADY_EXISTS, request.getCode());
        }

        updateEntityFromRequest(entity, request);
        Subject saved = subjectRepository.save(entity);
        return convertToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public SubjectResponse getByCode(String code) {
        Subject entity = subjectRepository.findByCode(code)
                .orElseThrow(() -> BusinessException.withDetail(ErrorCode.RESOURCE_NOT_FOUND, code));
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
        return entityMapper.toSubjectResponse(entity);
    }

    @Override
    protected void updateEntityFromRequest(Subject entity, SubjectRequest request) {
        entity.setCode(request.getCode());
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
    }

    @Override
    @Transactional
    @Audit(action = "DELETE_SUBJECT", entity = "Subject")
    public void delete(UUID publicId) {
        super.delete(publicId);
    }
}
