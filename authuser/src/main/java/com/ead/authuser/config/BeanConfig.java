package com.ead.authuser.config;

import com.ead.authuser.services.UserService;
import com.ead.authuser.services.impl.UserServiceIMPL;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {
    @Bean
    UserService userService() {
        return new UserServiceIMPL();
    }
}
