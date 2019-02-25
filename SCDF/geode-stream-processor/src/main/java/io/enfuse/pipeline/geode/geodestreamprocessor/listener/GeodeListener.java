package io.enfuse.pipeline.geode.geodestreamprocessor.listener;

import io.enfuse.pipeline.geode.geodestreamprocessor.domain.ExampleEntity;
import io.enfuse.pipeline.geode.geodestreamprocessor.domain.ExampleRepository;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.pdx.internal.PdxInstanceImpl;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

@Component
@EnableBinding(Processor.class)
public class GeodeListener {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private ClientCache clientCache;

    @Autowired
    public GeodeListener(ExampleRepository exampleRepository, ClientCache clientCache) {
        this.clientCache = clientCache;
    }

    @StreamListener(Processor.INPUT)
    @SendTo(Processor.OUTPUT)
    public ExampleEntity handle(String message) {
        logger.info("Attempting cache lookup for " + message);
        JSONObject jsonMessage = new JSONObject(message);
        Object obj = clientCache.getRegion("exampleRegion").get(jsonMessage.get("id").toString());
        //PdxInstanceImpl pdx = exampleRepository.findOneById(jsonMessage.get("id").toString());
        ExampleEntity exampleEntity = convertFromPdx(obj);
        logger.info("Result: *******  " + exampleEntity.toString() + "  *******");
        return exampleEntity;
    }

    private static ExampleEntity convertFromPdx(Object obj) {
        if (obj instanceof ExampleEntity) {
            return (ExampleEntity) obj;
        } else if (obj instanceof PdxInstanceImpl) {
            return convertFromPdx((PdxInstanceImpl) obj);
        } else {
            return new ExampleEntity();
        }
    }

    private static ExampleEntity convertFromPdx(PdxInstanceImpl pdxInstance) {
        String id = pdxInstance.getField("id").toString();
        String value = pdxInstance.getField("value").toString();
        return new ExampleEntity(id, value);
    }
}
