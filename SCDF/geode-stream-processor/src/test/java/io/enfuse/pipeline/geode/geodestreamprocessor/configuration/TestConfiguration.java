package io.enfuse.pipeline.geode.geodestreamprocessor.configuration;

import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.gemfire.client.ClientRegionFactoryBean;

@SpringBootApplication
@ComponentScan("io.enfuse.pipeline.geode.geodestreamprocessor")
@Profile("integration")
@Configuration
class TestConfiguration {

    @Bean("clientCache")
    ClientCache clientCache() {
        ClientCacheFactory clientCacheFactory = new ClientCacheFactory();
        ClientCache clientCache = clientCacheFactory.create();
        return clientCache;
    }

    @Bean("exampleRegion")
    public ClientRegionFactoryBean<Object, Object> exampleRegion(ClientCache clientCache) {

        ClientRegionFactoryBean<Object, Object> clientRegion = new ClientRegionFactoryBean<>();

        clientRegion.setCache(clientCache);
        clientRegion.setClose(false);
        clientRegion.setShortcut(ClientRegionShortcut.LOCAL);

        return clientRegion;
    }
}
