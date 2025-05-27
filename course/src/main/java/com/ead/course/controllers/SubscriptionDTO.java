package com.ead.course.controllers;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class SubscriptionDTO {
    @NotNull
    private UUID userId;
}
