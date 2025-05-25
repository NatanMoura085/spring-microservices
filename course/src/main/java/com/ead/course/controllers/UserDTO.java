package com.ead.course.controllers;

import com.ead.course.enums.UserStatus;
import com.ead.course.enums.UserType;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

import java.util.UUID;

@Data
@Log4j2
public class UserDTO {
    private UUID userId;
    private String username;
    private String email;
    private String fullName;
    private UserStatus userStatus;
    private UserType userType;
    private String phoneNumber;
    private String cpf;
    private String imageUrl;

}
