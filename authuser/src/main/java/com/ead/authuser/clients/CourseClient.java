package com.ead.authuser.clients;

import com.ead.authuser.dtos.CourseDTO;
import com.ead.authuser.dtos.ResponsePageDTO;
import com.ead.authuser.services.UtilsService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
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

   // @Retry(name = "retryInstance",fallbackMethod = "retryfallback")
   @CircuitBreaker(name = "circuitInstance",fallbackMethod = "circuitbreakerfallback")
    public Page<CourseDTO> getAllCoursesByUser(UUID userId, Pageable pageable) {
        List<CourseDTO> searchResult = new ArrayList<>();
        String url = utilsService.createUrl(userId, pageable);
        log.info("Request URL: {}", url);
         System.out.println("---Start Request ao Course Microservice");
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

    public Page<CourseDTO> retryfallback(UUID userId,Pageable pageable, Throwable t) {
        log.error("Inside retry retryfallback,cause, - {} ", t.toString());
        List<CourseDTO> searchResult = new ArrayList<>();
        return new PageImpl<>(searchResult);

    }

    public Page<CourseDTO> circuitbreakerfallback(UUID userId,Pageable pageable, Throwable t) {
        log.error("Inside circuit breaker fallback,cause, - {} ", t.toString());
        List<CourseDTO> searchResult = new ArrayList<>();
        return new PageImpl<>(searchResult);

    }
}
