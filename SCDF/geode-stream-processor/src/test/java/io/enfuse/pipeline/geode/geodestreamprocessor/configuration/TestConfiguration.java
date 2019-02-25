package io.enfuse.pipeline.geode.geodestreamprocessor.configuration;

import org.apache.geode.cache.GemFireCache;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.gemfire.client.ClientRegionFactoryBean;
import org.springframework.data.gemfire.config.annotation.EnableLogging;

@SpringBootApplication
@EnableLogging(logLevel = "debug")
@ComponentScan("io.enfuse.pipeline.geode.geodestreamprocessor")
@Profile("integration")
@Configuration
class TestConfiguration {

    @Bean("exampleRegion")
    public ClientRegionFactoryBean<Object, Object> exampleRegion(GemFireCache gemfireCache) {

        ClientRegionFactoryBean<Object, Object> clientRegion = new ClientRegionFactoryBean<>();

        clientRegion.setCache(gemfireCache);
        clientRegion.setClose(false);
        clientRegion.setShortcut(ClientRegionShortcut.LOCAL);

        return clientRegion;
    }
}