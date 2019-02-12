package com.example.gemfire.configuration;

//@Configuration
//@EnableGemfireFunctionExecutions(basePackages = "com.example.gemfire")

import com.example.gemfire.domain.ExampleRegion;
import org.apache.geode.cache.GemFireCache;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.gemfire.client.ClientRegionFactoryBean;
import org.springframework.data.gemfire.config.annotation.EnableLogging;

@Configuration
@EnableLogging
public class GemfireConfiguration {
/*

    @Bean
    PoolFactoryBean gemfirePool(@Value("localhost") String host, @Value("10334") int port) {

        PoolFactoryBean gemfirePool = new PoolFactoryBean();

        gemfirePool.setKeepAlive(false);
        gemfirePool.setSubscriptionEnabled(true);
        gemfirePool.setThreadLocalConnections(false);
        gemfirePool.addLocators(new ConnectionEndpoint(host, port));

        return gemfirePool;
    }

*/

    @Bean(name = "ExampleRegion")
    ClientRegionFactoryBean<String, ExampleRegion> exampleRegion(GemFireCache gemfireCache){

        ClientRegionFactoryBean<String, ExampleRegion> exampleRegion = new ClientRegionFactoryBean<>();
        exampleRegion.setCache(gemfireCache);
        exampleRegion.setName("ExampleRegion");
        exampleRegion.setShortcut(ClientRegionShortcut.PROXY);

        return exampleRegion;

    }



}