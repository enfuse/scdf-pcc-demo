package com.example.gemfire;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication;
import org.springframework.data.gemfire.config.annotation.EnableContinuousQueries;

@SpringBootApplication
@EnableContinuousQueries
@ClientCacheApplication(name = "CQConsumerClientCache", logLevel = "info", pingInterval = 5000L, readTimeout = 15000,
        retryAttempts = 1, subscriptionEnabled = true, locators = @ClientCacheApplication.Locator,
        readyForEvents = true, durableClientId = "22", durableClientTimeout = 5)
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
