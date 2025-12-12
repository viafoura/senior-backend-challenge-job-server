package com.example.jobserver.infrastructure.adapter.out.external;

import com.example.jobserver.application.port.out.ExternalJobProcessor;
import com.example.jobserver.application.port.out.ProcessingResult;
import com.example.jobserver.domain.exception.ExternalServiceException;
import com.example.jobserver.infrastructure.config.ExternalServiceProperties;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.UUID;

@Component
public class HttpExternalJobProcessor implements ExternalJobProcessor {

    private final RestClient restClient;
    private final ExternalServiceProperties properties;

    public HttpExternalJobProcessor(ExternalServiceProperties properties) {
        this.restClient = RestClient.builder()
                .baseUrl(properties.getUrl())
                .build();
        this.properties = properties;
    }

    @Override
    public ProcessingResult process(UUID jobId) {
        try {
            ExternalServiceResponse response = restClient.post()
                    .uri("/process")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ExternalServiceRequest(jobId.toString()))
                    .retrieve()
                    .body(ExternalServiceResponse.class);
            if (response == null) {
                throw new ExternalServiceException(("External service returned a null response"));
            }

            return new ProcessingResult(
                    UUID.fromString(response.jobId()),
                    response.value(),
                    response.status()
            );
        } catch (RestClientException exception) {
            throw new ExternalServiceException("External service call failed: " + exception.getMessage(), exception);
        } catch (IllegalArgumentException exception) {
            throw new ExternalServiceException("Invalid response from external service: " + exception.getMessage());
        }
    }

    private record ExternalServiceRequest(String jobId){}

    private record ExternalServiceResponse(String jobId, Integer value, String status){}

}
