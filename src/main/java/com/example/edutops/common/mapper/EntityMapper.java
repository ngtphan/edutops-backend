package com.example.edutops.common.mapper;

import com.example.edutops.classgroup.dto.ClassGroupResponse;
import com.example.edutops.classgroup.entity.ClassGroup;
import com.example.edutops.course.dto.CourseResponse;
import com.example.edutops.course.entity.Course;
import com.example.edutops.room.dto.RoomResponse;
import com.example.edutops.room.entity.Room;
import com.example.edutops.schedule.dto.ScheduleResponse;
import com.example.edutops.schedule.entity.Schedule;
import com.example.edutops.student.dto.StudentResponse;
import com.example.edutops.student.entity.Student;
import com.example.edutops.subject.dto.SubjectResponse;
import com.example.edutops.subject.entity.Subject;
import com.example.edutops.teacher.dto.TeacherResponse;
import com.example.edutops.teacher.entity.Teacher;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * Component tập trung chuyển đổi Entity → Response DTO cho toàn bộ hệ thống.
 * <p>
 * Loại bỏ việc duplicate mapping methods (mapCourseToResponse, mapTeacherToResponse, ...)
 * tại nhiều ServiceImpl khác nhau. Mỗi ServiceImpl chỉ cần inject và gọi lại.
 * </p>
 */
@Component
public class EntityMapper {

    // ========== Subject ==========

    public SubjectResponse toSubjectResponse(Subject entity) {
        if (entity == null) return null;

        SubjectResponse res = new SubjectResponse();
        res.setPublicId(entity.getPublicId());
        res.setCreatedAt(entity.getCreatedAt());
        res.setUpdatedAt(entity.getUpdatedAt());
        res.setCode(entity.getCode());
        res.setName(entity.getName());
        res.setDescription(entity.getDescription());
        return res;
    }

    // ========== Course ==========

    public CourseResponse toCourseResponse(Course entity) {
        if (entity == null) return null;

        CourseResponse res = new CourseResponse();
        res.setPublicId(entity.getPublicId());
        res.setCreatedAt(entity.getCreatedAt());
        res.setUpdatedAt(entity.getUpdatedAt());
        res.setCode(entity.getCode());
        res.setName(entity.getName());
        res.setDescription(entity.getDescription());
        res.setTotalSessions(entity.getTotalSessions());
        res.setFee(entity.getFee());
        res.setStatus(entity.getStatus());
        res.setStartDate(entity.getStartDate());
        res.setEndDate(entity.getEndDate());

        if (entity.getSubject() != null) {
            res.setSubject(toSubjectResponse(entity.getSubject()));
        }

        return res;
    }

    // ========== Teacher ==========

    public TeacherResponse toTeacherResponse(Teacher entity) {
        if (entity == null) return null;

        TeacherResponse res = new TeacherResponse();
        res.setPublicId(entity.getPublicId());
        res.setCreatedAt(entity.getCreatedAt());
        res.setUpdatedAt(entity.getUpdatedAt());
        res.setPhoneNumber(entity.getPhoneNumber());
        res.setBio(entity.getBio());

        if (entity.getUser() != null) {
            res.setUserPublicId(entity.getUser().getPublicId());
            res.setEmail(entity.getUser().getEmail());
            res.setFullName(entity.getUser().getFullName());
            res.setActive(entity.getUser().isActive());
        }

        if (entity.getSubjects() != null) {
            res.setSubjects(entity.getSubjects().stream()
                    .map(this::toSubjectResponse)
                    .collect(Collectors.toSet()));
        }

        return res;
    }

    // ========== Student ==========

    public StudentResponse toStudentResponse(Student entity) {
        if (entity == null) return null;

        StudentResponse res = new StudentResponse();
        res.setPublicId(entity.getPublicId());
        res.setCreatedAt(entity.getCreatedAt());
        res.setUpdatedAt(entity.getUpdatedAt());
        res.setPhoneNumber(entity.getPhoneNumber());
        res.setDateOfBirth(entity.getDateOfBirth());
        res.setGender(entity.getGender());

        if (entity.getUser() != null) {
            res.setUserPublicId(entity.getUser().getPublicId());
            res.setEmail(entity.getUser().getEmail());
            res.setFullName(entity.getUser().getFullName());
            res.setActive(entity.getUser().isActive());
        }

        return res;
    }

    // ========== Room ==========

    public RoomResponse toRoomResponse(Room entity) {
        if (entity == null) return null;

        RoomResponse res = new RoomResponse();
        res.setPublicId(entity.getPublicId());
        res.setCreatedAt(entity.getCreatedAt());
        res.setUpdatedAt(entity.getUpdatedAt());
        res.setName(entity.getName());
        res.setCapacity(entity.getCapacity());
        res.setLocation(entity.getLocation());
        res.setAvailable(entity.isAvailable());
        return res;
    }

    // ========== ClassGroup ==========

    public ClassGroupResponse toClassGroupResponse(ClassGroup entity) {
        if (entity == null) return null;

        ClassGroupResponse res = new ClassGroupResponse();
        res.setPublicId(entity.getPublicId());
        res.setCreatedAt(entity.getCreatedAt());
        res.setUpdatedAt(entity.getUpdatedAt());
        res.setCode(entity.getCode());
        res.setStartDate(entity.getStartDate());
        res.setEndDate(entity.getEndDate());
        res.setMaxStudents(entity.getMaxStudents());
        res.setStatus(entity.getStatus());

        if (entity.getCourse() != null) {
            res.setCourse(toCourseResponse(entity.getCourse()));
        }

        if (entity.getTeacher() != null) {
            res.setTeacher(toTeacherResponse(entity.getTeacher()));
        }

        return res;
    }

    // ========== Schedule ==========

    public ScheduleResponse toScheduleResponse(Schedule entity) {
        if (entity == null) return null;

        ScheduleResponse res = new ScheduleResponse();
        res.setPublicId(entity.getPublicId());
        res.setCreatedAt(entity.getCreatedAt());
        res.setUpdatedAt(entity.getUpdatedAt());
        res.setDayOfWeek(entity.getDayOfWeek());
        res.setStartTime(entity.getStartTime());
        res.setEndTime(entity.getEndTime());
        res.setEffectiveFrom(entity.getEffectiveFrom());
        res.setEffectiveTo(entity.getEffectiveTo());

        if (entity.getClassGroup() != null) {
            res.setClassGroup(toClassGroupResponse(entity.getClassGroup()));
        }

        if (entity.getRoom() != null) {
            res.setRoom(toRoomResponse(entity.getRoom()));
        }

        return res;
    }
}
