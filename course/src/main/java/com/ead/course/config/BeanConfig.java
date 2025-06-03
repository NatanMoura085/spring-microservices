package com.ead.course.config;

import com.ead.course.validation.CourseValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {
    @Bean
    public CourseValidator courseValidator(){
        return new CourseValidator();
    }
}
