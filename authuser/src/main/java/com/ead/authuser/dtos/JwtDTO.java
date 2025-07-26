package com.ead.authuser.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class JwtDTO {
    @NotNull
    private String token;
    private String type = "Bearer";

    public JwtDTO(String jwt) {
    }
}
