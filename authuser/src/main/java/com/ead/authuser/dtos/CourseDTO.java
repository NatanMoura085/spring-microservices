package com.ead.authuser.dtos;

import lombok.Data;

import java.util.UUID;

@Data
public class CourseDTO {
    private UUID courseId;
    private String name;
    private String description;
    private String imageUrl;
    private CourseStatus courseStatus;
    private UUID userInstructor;
    private CourseLevel courseLevel;
}
