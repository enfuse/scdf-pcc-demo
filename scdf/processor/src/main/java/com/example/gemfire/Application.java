package com.example.gemfire;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.gemfire.config.annotation.*;

@SpringBootApplication
@ClientCacheApplication(readyForEvents = true,
        subscriptionEnabled = true, logLevel ="debug", locators = {
        @ClientCacheApplication.Locator(host = "localhost", port = 10334)})
@EnableContinuousQueries
@EnableAutoRegionLookup
@EnableEntityDefinedRegions(basePackageClasses = ExampleRegion.class)
//@EnableGemfireRepositories
//@EnableGemfireCaching
//@EnableCachingDefinedRegions


public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
