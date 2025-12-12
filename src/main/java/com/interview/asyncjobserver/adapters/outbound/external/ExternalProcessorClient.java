package com.interview.asyncjobserver.adapters.outbound.external;


import com.interview.asyncjobserver.ports.ExternalProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class ExternalProcessorClient implements ExternalProcessor {
    private final WebClient webClient;

    public ExternalProcessorClient(@Value("${external.process.url}") String processUrl) {
        this.webClient = WebClient.create(processUrl);
    }

    @Override
    public String process(UUID jobId) throws Exception {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("jobId", jobId.toString());

        return webClient.post()
                .uri("process")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofSeconds(10))
                .block();
    }
}

