package com.example.edutops.room.entity;

import com.example.edutops.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * Phòng học vật lý tại trung tâm.
 */
@Entity
@Table(name = "rooms")
public class Room extends BaseEntity {

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false, unique = true, length = 50)
    private String name;

    @Positive
    @Column(nullable = false)
    private int capacity;

    @Size(max = 200)
    @Column(length = 200)
    private String location;

    @Column(nullable = false)
    private boolean available = true;

    // --- Constructors ---

    public Room() {
    }

    public Room(String name, int capacity, String location) {
        this.name = name;
        this.capacity = capacity;
        this.location = location;
        this.available = true;
    }

    // --- Getters & Setters ---

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
