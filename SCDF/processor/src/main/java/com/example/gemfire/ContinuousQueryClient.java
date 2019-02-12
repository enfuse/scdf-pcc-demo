package com.example.gemfire;

import org.apache.geode.cache.query.CqEvent;
import org.springframework.data.gemfire.listener.annotation.ContinuousQuery;
import org.springframework.stereotype.Component;

@Component
public class ContinuousQueryClient {

    @ContinuousQuery(name = "exampleRegion",
            query = "SELECT * FROM /exampleRegion",
            durable = true)
    public void handleChanges(CqEvent event) {
        System.out.println("Received message for CQ 'exampleRegion'" + event);
    }
}