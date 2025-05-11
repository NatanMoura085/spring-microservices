package com.ead.course.controllers;

import com.ead.course.dtos.CourseDTO;
import com.ead.course.models.CourseModel;
import com.ead.course.services.CourseService;
import com.ead.course.specifications.SpecificationTemplate;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/courses")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CourseController {
    @Autowired
    CourseService courseService;

    @PostMapping
    public ResponseEntity<Object> saveCourse(@RequestBody @Valid CourseDTO courseDTO){
    var courseModel = new CourseModel();
        BeanUtils.copyProperties(courseDTO,courseModel);
        courseModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        courseModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        courseService.save(courseModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(courseModel);
    }

    @DeleteMapping
    public ResponseEntity<Object> deleteCourse(@PathVariable(value = "couseId")UUID courseId){
        Optional<CourseModel> courseModelOptional = courseService.findById(courseId);
        if (!courseModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Couse Not Found");
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
            courseModel.setName(courseModel.getName());
            courseModel.setDescription(courseDTO.getDescription());
            courseModel.setImageUrl(courseDTO.getImageUrl());
            courseModel.setCourseStatus(courseDTO.getCourseStatus());
            return ResponseEntity.status(HttpStatus.OK).body(courseService.save(courseModel));
    }

    @GetMapping
    public ResponseEntity<Page<CourseModel>> getAllCourse(Specification<CourseModel> spec, @PageableDefault(page = 0, size = 10, sort = "courseId", direction = Sort.Direction.ASC) Pageable pageable) {
          return ResponseEntity.status(HttpStatus.OK).body(courseService.findByAll(spec,pageable));
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<Object> getOneUser(@PathVariable(value = "courseId") UUID courseId){
        Optional<CourseModel> optionalCourseModel = courseService.findById(courseId);
        if(!optionalCourseModel.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("course não encontrado");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(optionalCourseModel.get());

    }
}
