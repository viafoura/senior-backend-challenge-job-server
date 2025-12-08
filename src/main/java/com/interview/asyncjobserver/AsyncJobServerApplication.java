package com.interview.asyncjobserver;

import com.interview.asyncjobserver.domain.User;
import com.interview.asyncjobserver.ports.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.UUID;

@SpringBootApplication(exclude = {FlywayAutoConfiguration.class})
@EntityScan(basePackages = "com.interview.asyncjobserver.domain")
public class AsyncJobServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AsyncJobServerApplication.class, args);
    }

    /*
     * Load Data to DB for testing purpose
     */
    @Bean
    CommandLineRunner init(UserRepository repo) {
        return args -> {
            if(repo.count() == 0) {
                repo.save(new User(UUID.fromString("11111111-1111-1111-1111-111111111111"), "alice"));
                repo.save(new User(UUID.fromString("22222222-2222-2222-2222-222222222222"), "bob"));
            }
        };
    }
}
