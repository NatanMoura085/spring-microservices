package com.ead.course.services;

import com.ead.course.models.UserModeL;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;
import java.util.UUID;

public interface UserService {
    Page<UserModeL> findAll(Specification<UserModeL> spec, Pageable pageable);

    UserModeL save(UserModeL userModel);

    void delete(UUID userId);

    Optional<UserModeL> findById(UUID userInstructor);
}
