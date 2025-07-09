package com.ead.course.services.impl;

import com.ead.course.models.UserModeL;
import com.ead.course.repositories.UserRepository;
import com.ead.course.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class UserServiceimpl implements UserService {
    @Autowired
    UserRepository userRepository;


    @Override
    public Page<UserModeL> findAll(Specification<UserModeL> spec, Pageable pageable) {
        return userRepository.findAll(spec,pageable);
    }
}
