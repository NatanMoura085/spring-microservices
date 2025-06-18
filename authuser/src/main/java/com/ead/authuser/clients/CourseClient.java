package com.ead.authuser.clients;

import com.ead.authuser.dtos.CourseDTO;
import com.ead.authuser.dtos.ResponsePageDTO;
import com.ead.authuser.services.UtilsService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

@Component
@Log4j2
public class CourseClient {

    @Autowired
    RestTemplate restTemplate;


    @Autowired
    UtilsService utilsService;
    @Value("${ead.api.url.course}")
    private String REQUEST_URL_AUTHUSER;

    public Page<CourseDTO> getAllCoursesByUser(UUID userId, Pageable pageable) {
        List<CourseDTO> searchResult = new ArrayList<>();
        String url = utilsService.createUrl(userId, pageable);
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

    public void deleteUserInCourse(UUID userId) {
        String url = REQUEST_URL_AUTHUSER + "/courses/users/" + userId;
        restTemplate.exchange(url, HttpMethod.DELETE, null, String.class);
    }
}
