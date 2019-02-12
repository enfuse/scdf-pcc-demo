package com.example.gemfire.client;

import org.apache.geode.cache.query.CqEvent;
import org.apache.geode.pdx.PdxInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.data.gemfire.listener.annotation.ContinuousQuery;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class ContinuousQueryClient {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Bean
    ApplicationRunner runner() {
        return args -> new Scanner(System.in, "UTF-8").nextLine();
    }

    @ContinuousQuery(name = "exampleRegion",
            query = "SELECT * FROM /exampleRegion",
            durable = true)
    public void handleChanges(CqEvent event) {

        Object key = event.getKey();
        PdxInstance valuePdxInstance = (PdxInstance) event.getNewValue();
        String value = (String) valuePdxInstance.getField("value");

        logger.info(
                "Received message for CQ 'exampleRegion' {}\nEvent key [{}] and value [{}]",
                event, key, value);
    }
}