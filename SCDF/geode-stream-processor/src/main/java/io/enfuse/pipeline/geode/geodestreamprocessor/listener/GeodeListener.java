package io.enfuse.pipeline.geode.geodestreamprocessor.listener;

import io.enfuse.pipeline.geode.geodestreamprocessor.domain.ExampleEntity;
import io.enfuse.pipeline.geode.geodestreamprocessor.domain.ExampleRepository;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@EnableBinding(Processor.class)
public class GeodeListener {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private ExampleRepository exampleRepository;

    @Autowired
    public GeodeListener(ExampleRepository exampleRepository) {
        this.exampleRepository = exampleRepository;
    }

    @StreamListener(Processor.INPUT)
    @SendTo(Processor.OUTPUT)
    public String handle(String message) {
        JSONObject jsonMessage = new JSONObject(message);

        Optional<ExampleEntity> resultValue = exampleRepository.findById(1L);

        resultValue.ifPresent(exampleEntity ->
        {
            logger.info("************************" + exampleEntity.getValue());
        });

//        logger.info("***" + jsonMessage.get("value") + "***");
        return message + resultValue.get().getValue();
    }

}
