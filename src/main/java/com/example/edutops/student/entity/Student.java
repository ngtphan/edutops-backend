package com.example.edutops.student.entity;

import com.example.edutops.common.entity.BaseEntity;
import com.example.edutops.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;

/**
 * Học viên — profile mở rộng liên kết với User.
 */
@Entity
@Table(name = "students")
@SQLDelete(sql = "UPDATE students SET deleted = true WHERE id = ?")
@SQLRestriction("deleted = false")
public class Student extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Size(max = 20)
    @Column(length = 20)
    private String phoneNumber;

    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private Gender gender;

    // --- Constructors ---

    public Student() {
    }

    public Student(User user, String phoneNumber, LocalDate dateOfBirth, Gender gender) {
        this.user = user;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
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

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }
}
