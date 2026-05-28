package com.example.edutops.room.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class RoomCreateRequest {

    @NotBlank(message = "Tên phòng học không được để trống")
    @Size(max = 50, message = "Tên phòng học không được vượt quá 50 ký tự")
    private String name;

    @Positive(message = "Sức chứa phòng học phải lớn hơn 0")
    private int capacity;

    @Size(max = 200, message = "Vị trí không được vượt quá 200 ký tự")
    private String location;

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
}
