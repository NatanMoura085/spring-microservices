package com.ead.course.validation;

import com.ead.course.controllers.UserDTO;
import com.ead.course.dtos.CourseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.UUID;

public class CourseValidator implements Validator {
    @Autowired
    @Qualifier("defaultValidator")
    private Validator validator;


    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object o, Errors errors) {
        CourseDTO courseDTO = (CourseDTO) o;
        validator.validate(courseDTO, errors);

        if (!errors.hasErrors()) {
            validateUserInstructor(courseDTO.getUserInstructor(), errors);
        }

    }

    private void validateUserInstructor(UUID userInstructor, Errors errors) {
        ResponseEntity<UserDTO> responseUserInstructor;
//        try {
//            responseUserInstructor = authUserClient.getOneUserById(userInstructor);
//            if (responseUserInstructor.getBody().getUserType().equals(UserType.STUDENT)) {
//                errors.rejectValue("userInstructor", "userInstructor", "User Must be INSTRUCTOR OR ADMIN ");
//            }
//        } catch (HttpStatusCodeException e) {
//            errors.rejectValue("userInstructor", "userInstructor", "userInstructor Not Found");
//        }
   }
}
