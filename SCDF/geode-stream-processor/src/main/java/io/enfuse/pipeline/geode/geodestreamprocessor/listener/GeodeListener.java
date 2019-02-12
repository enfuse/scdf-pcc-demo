package io.enfuse.pipeline.geode.geodestreamprocessor.listener;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

@Component
@EnableBinding(Processor.class)
public class GeodeListener {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @StreamListener(Processor.INPUT)
    @SendTo(Processor.OUTPUT)
    public String handle(String message) {
        JSONObject jsonMessage = new JSONObject(message);
        logger.info("***" + jsonMessage.get("value") + "***");
        return message;
    }

}
