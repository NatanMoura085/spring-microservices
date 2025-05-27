package com.ead.course.clients;

import com.ead.course.controllers.UserDTO;
import com.ead.course.dtos.ResponsePageDTO;
import com.ead.course.services.UtilsService;
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
@Log4j2
@Component
public class AuthUserClient {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    UtilsService utilsService;
    @Value("${ead.api.url.authuser}")
    private String REQUEST_URL_AUTHUSER;
    public Page<UserDTO> getAllCoursesByCourse(UUID courseId, Pageable pageable) {
        List<UserDTO> searchResult = new ArrayList<>();
        String url = utilsService.createUrlGetAllUsersByCourse(courseId, pageable);
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


    public ResponseEntity<UserDTO> getOneUserById(UUID userId){
        String url = REQUEST_URL_AUTHUSER + "/users/" + userId;
        return restTemplate.exchange(url,HttpMethod.GET,null, UserDTO.class);
    }

}
