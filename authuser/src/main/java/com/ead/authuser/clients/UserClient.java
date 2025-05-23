package com.ead.authuser.clients;

import com.ead.authuser.dtos.CourseDTO;
import com.ead.authuser.dtos.ResponsePageDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@Log4j2
public class UserClient {

    @Autowired
    RestTemplate restTemplate;


    private String REQUEST_URI = "http://localhost:8082";

    public Page<CourseDTO> getAllCoursesByUser(UUID userId, Pageable pageable) {
        List<CourseDTO> searchResult = new ArrayList<>();
        String sort = pageable.getSort().stream()
                .map(order -> order.getProperty() + "," + order.getDirection())
                .collect(Collectors.joining("&sort="));

        String url = String.format("%s/courses?userId=%s&page=%d&size=%d&sort=%s",
                REQUEST_URI, userId, pageable.getPageNumber(), pageable.getPageSize(), sort);

        log.info("Request URL: {}", url);

        try {
            ParameterizedTypeReference<ResponsePageDTO<CourseDTO>> responseType = new ParameterizedTypeReference<>() {};
            ResponseEntity<ResponsePageDTO<CourseDTO>> result = restTemplate.exchange(url, HttpMethod.GET, null, responseType);

            ResponsePageDTO<CourseDTO> body = result.getBody();
            if (body != null) {
                searchResult = body.getContent();
                log.debug("Response Number of Elements: {}", searchResult.size());
                return new PageImpl<>(searchResult, pageable, body.getTotalElements());
            }
        } catch (HttpStatusCodeException e) {
            log.error("Error request /courses {}", e);
        }

        log.info("Ending request /courses userId {}", userId);
        return Page.empty(pageable);
    }
}
