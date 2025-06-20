package com.ead.course.controllers;

import com.ead.course.clients.AuthUserClient;
import com.ead.course.enums.UserStatus;
import com.ead.course.models.CourseModel;
import com.ead.course.models.CourseUserModel;
import com.ead.course.services.CourseService;
import com.ead.course.services.CourseUserService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.Optional;
import java.util.UUID;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class CourseUserController {
    @Autowired
    AuthUserClient authUserClient;
    @Autowired
    CourseService courseService;

    @Autowired
    CourseUserService courseUserService;
    @GetMapping("/courses/{courseId}/users")
    public ResponseEntity<Object> getAllUsersByCouse(@PageableDefault(page = 0, size = 10, sort = "userId", direction = Sort.Direction.ASC) Pageable pageable, @PathVariable(value = "courseId") UUID courseId) {
        Optional<CourseModel> courseModelOptional = courseService.findById(courseId);
        if (!courseModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found");
        }

        return ResponseEntity.status(HttpStatus.OK).body(authUserClient.getAllCoursesByCourse(courseId, pageable));
    }

    @PostMapping("/courses/{courseId}/users/subscription")
    public ResponseEntity<Object> saveSubscriptionUserInCourse(@PathVariable(value = "courseId") UUID courseId, @RequestBody @Valid SubscriptionDTO subscriptionDTO) {
        Optional<CourseModel> courseModel = courseService.findById(courseId);
        ResponseEntity<UserDTO> response;
        if (!courseModel.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found");
        }

        if (courseUserService.existsByCourseAndUserId(courseModel.get(), subscriptionDTO.getUserId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: subscription already exists");
        }
        try {
            response = authUserClient.getOneUserById(subscriptionDTO.getUserId());
            if (response.getBody().getUserStatus().equals(UserStatus.BLOCKED)){
                return ResponseEntity.status(HttpStatus.CONFLICT).body("User Is Blocked");
            }
        } catch (HttpStatusCodeException e) {
           if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)){
               return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found");
           }
        }
        CourseUserModel courseUserModel = courseUserService.saveAndSendSubscriptionUserInCourse(courseModel.get().convertToCourseUserModel(subscriptionDTO.getUserId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(courseUserModel);
    }

    @DeleteMapping("/courses/users/{userId}")
    public ResponseEntity<Object> deleteCourseUserByUser(@PathVariable(value = "userId") UUID userId) {
        if (!courseUserService.exitsByUserId(userId)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("CourseUser not found");
        }
        courseUserService.deleteCourseByUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body("CourseUser deleted successfully.");
    }
}
