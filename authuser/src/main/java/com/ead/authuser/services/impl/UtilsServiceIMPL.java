package com.ead.authuser.services.impl;

import com.ead.authuser.services.UtilsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UtilsServiceIMPL implements UtilsService {
 @Value("${ead.api.url.course}")
    private String REQUEST_URL_COURSE;

    @Override
    public String createUrl(UUID userId, Pageable pageable) {
        String sort = pageable.getSort().stream()
                .map(order -> order.getProperty() + "," + order.getDirection())
                .collect(Collectors.joining("&sort="));
        return String.format("%s/courses?userId=%s&page=%d&size=%d&sort=%s",
                REQUEST_URL_COURSE, userId, pageable.getPageNumber(), pageable.getPageSize(), sort);
    }
}
