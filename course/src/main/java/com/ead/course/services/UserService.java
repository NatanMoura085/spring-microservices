package com.ead.course.services;

import com.ead.course.models.UserModeL;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface UserService {
    Page<UserModeL> findAll(Specification<UserModeL> spec, Pageable pageable);
}
