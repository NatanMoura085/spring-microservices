package com.ead.course.services.impl;

import com.ead.course.services.UtilsService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UtilsServiceIMPL implements UtilsService {

    private String REQUEST_URI = "http://localhost:8087";

    @Override
    public String createUrl(UUID courseId, Pageable pageable) {
        String sort = pageable.getSort().stream()
                .map(order -> order.getProperty() + "," + order.getDirection())
                .collect(Collectors.joining("&sort="));
        return String.format("%s/users?courseId=%s&page=%d&size=%d&sort=%s",
                REQUEST_URI, courseId, pageable.getPageNumber(), pageable.getPageSize(), sort);
    }
}
