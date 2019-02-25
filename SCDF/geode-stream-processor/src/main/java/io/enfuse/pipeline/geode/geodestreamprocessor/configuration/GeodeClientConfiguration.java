package io.enfuse.pipeline.geode.geodestreamprocessor.configuration;

import io.enfuse.pipeline.geode.geodestreamprocessor.domain.ExampleEntity;
import io.enfuse.pipeline.geode.geodestreamprocessor.domain.ExampleRepository;
import org.apache.geode.cache.RegionShortcut;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.data.gemfire.config.annotation.*;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;
import org.springframework.data.gemfire.support.ConnectionEndpoint;

import java.util.Collections;

@Profile("!integration")
@ClientCacheApplication
@EnableEntityDefinedRegions(basePackageClasses = ExampleEntity.class,
        clientRegionShortcut = ClientRegionShortcut.PROXY,
        serverRegionShortcut = RegionShortcut.PARTITION)
@EnableGemfireRepositories(basePackageClasses = ExampleRepository.class)
//@ComponentScan(basePackageClasses = CustomerService.class)
@EnableIndexing
//@EnableClusterConfiguration(useHttp = true)
public class GeodeClientConfiguration {
    @Bean
    ClientCacheConfigurer clientCacheServerConfigurer(
            @Value("${spring.data.geode.locator.host:localhost}") String hostname,
            @Value("${spring.data.geode.locator.port:10334}") int port) {

        return (beanName, clientCacheFactoryBean) -> clientCacheFactoryBean
                .setLocators(Collections.singletonList(
                        new ConnectionEndpoint(hostname, port)));
    }
}
