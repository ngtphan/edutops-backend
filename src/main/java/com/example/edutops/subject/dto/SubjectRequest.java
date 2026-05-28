package com.example.edutops.subject.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class SubjectRequest {

    @NotBlank(message = "{subject.code.notblank}")
    @Size(max = 20, message = "{subject.code.size}")
    @Pattern(regexp = "^[A-Z0-9]+$", message = "{subject.code.pattern}")
    private String code;

    @NotBlank(message = "{subject.name.notblank}")
    @Size(max = 100, message = "{subject.name.size}")
    private String name;

    private String description;

    // --- Getters & Setters ---

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
