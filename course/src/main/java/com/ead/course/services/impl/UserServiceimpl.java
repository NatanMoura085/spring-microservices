package com.ead.course.services.impl;

import com.ead.course.repositories.UserRepository;
import com.ead.course.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceimpl implements UserService {
    @Autowired
    UserRepository userRepository;


}
