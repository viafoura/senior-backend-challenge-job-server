package com.jobserver.infrastructure.adapter.out.external;

import com.jobserver.domain.exception.ExternalServiceException;
import com.jobserver.domain.port.out.ExternalJobProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@Slf4j
public class ExternalJobProcessorAdapter implements ExternalJobProcessor {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public ExternalJobProcessorAdapter(
            RestTemplate restTemplate,
            @Value("${external-service.base-url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    @Override
    @Retryable(
            retryFor = RestClientException.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    @SuppressWarnings("unchecked")
    public Map<String, Object> process(UUID jobId) {
        String url = baseUrl + "/process";
        log.info("Calling external service for job: {}", jobId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("jobId", jobId.toString());

        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            log.info("External service returned success for job: {}", jobId);
            return response.getBody();
        }

        throw new ExternalServiceException("External service returned: " + response.getStatusCode());
    }

    @Recover
    public Map<String, Object> recover(RestClientException e, UUID jobId) {
        log.error("All retry attempts exhausted for job {}: {}", jobId, e.getMessage());
        throw new ExternalServiceException("External service failed after retries: " + e.getMessage(), e);
    }
}
