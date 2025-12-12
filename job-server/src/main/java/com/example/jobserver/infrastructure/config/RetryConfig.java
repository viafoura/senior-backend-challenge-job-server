package com.example.jobserver.infrastructure.config;


import com.example.jobserver.domain.exception.ExternalServiceException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import java.util.Map;

@Configuration
@EnableRetry
public class RetryConfig {

    @Bean
    public RetryTemplate retryTemplate() {
        RetryTemplate rt = new RetryTemplate();
        ExponentialBackOffPolicy bp = new ExponentialBackOffPolicy();
        bp.setInitialInterval(1000L); // 1 sec
        bp.setMultiplier(2.0);
        bp.setMaxInterval(10000L); // 10 sec
        rt.setBackOffPolicy(bp);

        Map<Class<? extends Throwable>, Boolean> retryExceptions = Map.of(
                ExternalServiceException.class, true,
                RuntimeException.class, true
                );
        SimpleRetryPolicy rp = new SimpleRetryPolicy(3, retryExceptions);
        rt.setRetryPolicy(rp);

        return rt;

    }
}
