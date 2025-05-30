package com.ead.course.controllers;

import com.ead.course.dtos.CourseDTO;
import com.ead.course.models.CourseModel;
import com.ead.course.services.CourseService;
import com.ead.course.specifications.SpecificationTemplate;
import com.ead.course.validation.CourseValidator;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/courses")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CourseController {
    @Autowired
    CourseService courseService;
    @Autowired
    CourseValidator courseValidator;

    @PostMapping
    public ResponseEntity<Object> saveCourse(@RequestBody CourseDTO courseDTO, Errors errors) {
        courseValidator.validate(courseDTO, errors);
        if (errors.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors.getAllErrors());
        }
    var courseModel = new CourseModel();
        BeanUtils.copyProperties(courseDTO,courseModel);
        courseModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        courseModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        courseService.save(courseModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(courseModel);
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<Object> deleteCourse(@PathVariable(value = "courseId")UUID courseId){
        Optional<CourseModel> courseModelOptional = courseService.findById(courseId);
        if (!courseModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found");
        }
        courseService.delete(courseModelOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Deletado com sucesso");
    }

    @PutMapping("/{courseId}")
    public ResponseEntity<Object> updateCourse(@PathVariable(value = "courseId")UUID courseId, @RequestBody @Valid CourseDTO courseDTO){
        Optional<CourseModel> optionalCourseModel = courseService.findById(courseId);
            if(!optionalCourseModel.isPresent()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("course não encontrado");
            }
            var courseModel = optionalCourseModel.get();
            courseModel.setName(courseDTO.getName());
            courseModel.setDescription(courseDTO.getDescription());
            courseModel.setImageUrl(courseDTO.getImageUrl());
            courseModel.setCourseStatus(courseDTO.getCourseStatus());
            courseModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
            return ResponseEntity.status(HttpStatus.OK).body(courseService.save(courseModel));
    }

    @GetMapping
    public ResponseEntity<Page<CourseModel>> getAllCourse(SpecificationTemplate.CourseExpec spec , @PageableDefault(page = 0, size = 10, sort = "courseId", direction = Sort.Direction.ASC) Pageable pageable, @RequestParam(required = false) UUID userId) {
        if (userId != null) {
            return ResponseEntity.status(HttpStatus.OK).body(courseService.findAll(SpecificationTemplate.courseUserId(userId).and(spec), pageable));
        } else {
          return ResponseEntity.status(HttpStatus.OK).body(courseService.findAll(spec,pageable));

        }
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<Object> getOneCourse(@PathVariable(value = "courseId") UUID courseId){
        Optional<CourseModel> optionalCourseModel = courseService.findById(courseId);
        if(!optionalCourseModel.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("course não encontrado");
        }
        return ResponseEntity.status(HttpStatus.OK).body(optionalCourseModel.get());

    }
}
