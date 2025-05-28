package com.ead.authuser.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class InstructorDTO {
    @NotNull
    private UUID userId;
}
