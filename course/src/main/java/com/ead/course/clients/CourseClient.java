package com.ead.course.clients;

import com.ead.course.controllers.UserDTO;
import com.ead.course.dtos.ResponsePageDTO;
import com.ead.course.services.UtilsService;
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
@Log4j2
@Component
public class CourseClient {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    UtilsService utilsService;
    public Page<UserDTO> getAllCoursesByCourse(UUID courseId, Pageable pageable) {
        List<UserDTO> searchResult = new ArrayList<>();
        String url = utilsService.createUrl(courseId, pageable);
        log.info("Request URL: {}", url);

        try {
            ParameterizedTypeReference<ResponsePageDTO<UserDTO>> responseType = new ParameterizedTypeReference<>() {};
            ResponseEntity<ResponsePageDTO<UserDTO>> result = restTemplate.exchange(url, HttpMethod.GET, null, responseType);

            ResponsePageDTO<UserDTO> body = result.getBody();
            if (body != null) {
                searchResult = body.getContent();
                log.debug("Response Number of Elements: {}", searchResult.size());
                return new PageImpl<>(searchResult, pageable, body.getTotalElements());
            }
        } catch (HttpStatusCodeException e) {
            log.error("Error request /courses {}", e);
        }

        log.info("Ending request /users userId {}", courseId);
        return Page.empty(pageable);
    }

}
