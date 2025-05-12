package com.ead.authuser.controllers;

import com.ead.authuser.dtos.UserDto;
import com.ead.authuser.enums.UserStatus;
import com.ead.authuser.enums.UserType;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/auth")
public class AuthentificationController {
    @Autowired
    UserService userService;



    @PostMapping("/signup")
    public ResponseEntity<Object> registreUser(@RequestBody @Valid
                                                   @JsonView(UserDto.UserView.RegistrationPost.class) UserDto userDto) {
        log.debug("POST registreUser UserDto receveived {} ",userDto.toString());
        if (userService.existsByUserName(userDto.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error Username is Already taken!");
        }
        if (userService.existsByEmail(userDto.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error Email is Already taken!");
        }
        var userModel = new UserModel();
        BeanUtils.copyProperties(userDto, userModel);
        userModel.setUserStatus(UserStatus.ACTIVE);
        userModel.setUserType(UserType.STUDENT);
        userModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        userService.save(userModel);
        log.debug("POST registreUser userModel saved {}",userModel.toString());
        return ResponseEntity.status(HttpStatus.CREATED).body(userModel);

    }
}
