package com.example.edutops.teacher.entity;

import com.example.edutops.common.entity.BaseEntity;
import com.example.edutops.subject.entity.Subject;
import com.example.edutops.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.HashSet;
import java.util.Set;

/**
 * Giáo viên — profile mở rộng liên kết với User.
 * <p>
 * Một giáo viên có thể dạy nhiều môn (Many-to-Many với Subject).
 * </p>
 */
@Entity
@Table(name = "teachers")
@SQLDelete(sql = "UPDATE teachers SET deleted = true WHERE id = ?")
@SQLRestriction("deleted = false")
public class Teacher extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Size(max = 20)
    @Column(length = 20)
    private String phoneNumber;

    @Column(columnDefinition = "TEXT")
    private String bio;

    /**
     * Các môn mà giáo viên này có thể dạy.
     * Join table: teacher_subjects (teacher_id, subject_id) — cả 2 FK dùng Long.
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "teacher_subjects",
            joinColumns = @JoinColumn(name = "teacher_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id")
    )
    private Set<Subject> subjects = new HashSet<>();

    // --- Constructors ---

    public Teacher() {
    }

    public Teacher(User user, String phoneNumber, String bio) {
        this.user = user;
        this.phoneNumber = phoneNumber;
        this.bio = bio;
    }

    // --- Getters & Setters ---

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Set<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(Set<Subject> subjects) {
        this.subjects = subjects;
    }

    // --- Helper methods ---

    public void addSubject(Subject subject) {
        this.subjects.add(subject);
    }

    public void removeSubject(Subject subject) {
        this.subjects.remove(subject);
    }
}
