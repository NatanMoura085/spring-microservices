package com.ead.authuser.controllers;

import com.ead.authuser.dtos.InstructorDTO;
import com.ead.authuser.enums.UserType;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/instructors")
public class InstructorController {

    @Autowired
    UserService userService;

    @PostMapping("/subscription")
    public ResponseEntity<Object> saveSubscriptionInstructor(@RequestBody @Valid InstructorDTO instructorDTO) {
        Optional<UserModel> userModel = userService.findById(instructorDTO.getUserId());
        if (!userModel.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("");
        } else {
            var userModelOP = userModel.get();
            userModelOP.setUserType(UserType.INSTRUCTOR);
            userModelOP.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
            userService.save(userModelOP);
            return ResponseEntity.status(HttpStatus.OK).body(userModelOP);
        }
    }
}
