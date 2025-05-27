package com.ead.course.services.impl;

import com.ead.course.services.UtilsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UtilsServiceIMPL implements UtilsService {
    @Value("${ead.api.url.authuser}")
    private String REQUEST_URL_AUTHUSER;

    @Override
    public String createUrlGetAllUsersByCourse(UUID courseId, Pageable pageable) {
        String sort = pageable.getSort().stream()
                .map(order -> order.getProperty() + "," + order.getDirection())
                .collect(Collectors.joining("&sort="));
        return String.format("%s/users?courseId=%s&page=%d&size=%d&sort=%s",
                REQUEST_URL_AUTHUSER, courseId, pageable.getPageNumber(), pageable.getPageSize(), sort);
    }
}
