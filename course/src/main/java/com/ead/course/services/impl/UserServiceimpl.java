package com.ead.course.services.impl;

import com.ead.course.models.UserModeL;
import com.ead.course.repositories.CourserRepository;
import com.ead.course.repositories.UserRepository;
import com.ead.course.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceimpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    CourserRepository courserRepository;

    @Override
    public Page<UserModeL> findAll(Specification<UserModeL> spec, Pageable pageable) {
        return userRepository.findAll(spec,pageable);
    }

    @Override
    public UserModeL save(UserModeL userModel) {
        return userRepository.save(userModel);
    }

    @Transactional
    @Override
    public void delete(UUID userId) {
        courserRepository.deleteCourseUserByUser(userId);
        userRepository.deleteById(userId);
    }

    @Override
    public Optional<UserModeL> findById(UUID userInstructor) {
        return userRepository.findById(userInstructor);
    }
}
