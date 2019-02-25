package io.enfuse.pipeline.geode.geodestreamprocessor.listener;

import io.enfuse.pipeline.geode.geodestreamprocessor.domain.ExampleEntity;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.internal.cache.GemFireCacheImpl;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.cloud.stream.test.binder.MessageCollector;
import org.springframework.data.gemfire.util.RegionUtils;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;
import java.util.concurrent.BlockingQueue;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.data.gemfire.util.RuntimeExceptionFactory.newIllegalStateException;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("integration")
public class GeodeListenerIntegrationTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

//    @Autowired
//    private ExampleRepository exampleRepository;

//    @MockBean
//    private ExampleRepository exampleRepository;

    @Autowired
    private ClientCache clientCache;

    @Autowired
    private Processor processor;

    @Autowired
    private MessageCollector messageCollector;

    @Before
    public void setUp() {
        Optional.ofNullable(this.clientCache)
                .filter(it -> it instanceof GemFireCacheImpl)
                .map(it -> (GemFireCacheImpl) it)
                .map(it -> assertThat(it.isClient()).isTrue())
                .orElseThrow(() -> newIllegalStateException("ClientCache was null"));

        Region<Object, Object> example = this.clientCache.getRegion("exampleRegion");

        assertThat(example).isNotNull();
        assertThat(example.getName()).isEqualTo("exampleRegion");
        assertThat(example.getFullPath()).isEqualTo(RegionUtils.toRegionPath("exampleRegion"));

        ExampleEntity exampleOne = new ExampleEntity();
        ExampleEntity exampleTwo = new ExampleEntity();
        ExampleEntity exampleThree = new ExampleEntity();
        ExampleEntity exampleFour = new ExampleEntity();

        exampleOne.setId("1");
        exampleOne.setValue("example one value");

        exampleTwo.setId("2");
        exampleTwo.setValue("example two value");

        exampleThree.setId("3");
        exampleThree.setValue("example three value");

        exampleFour.setId("4");
        exampleFour.setValue("example four value");


        example.put("1", exampleOne);
        example.put("2", exampleTwo);
        example.put("3", exampleThree);
        example.put("4", exampleFour);
    }

    @Test
    public void handle_givenString_sendsString() throws JSONException {
        String payloadOne = "{\"id\":\"1\"}";
        String payloadTwo = "{\"id\":\"2\"}";


        this.processor.input().send(new GenericMessage<>(payloadOne));
        this.processor.input().send(new GenericMessage<>(payloadTwo));

        BlockingQueue<Message<?>> messages = messageCollector.forChannel(processor.output());

        for (Message message : messages) {
            logger.info(message.getPayload().toString());
        }

        assertThat(messages.contains(payloadOne));
        assertThat(messages.contains(payloadTwo));


    }

    @Test
    public void handle_givenPayloadWithValidId_returnsPayload() {
        String CLIENT_VALID_PAYLOAD_ONE = "{\"id\":\"1\"}";
        String VALID_CACHE_PAYLOAD_ONE = "{\"id\":\"1\",\"value\":\"example one value\"}";

        processor.input().send(new GenericMessage<>(CLIENT_VALID_PAYLOAD_ONE));
        GenericMessage<String> result = (GenericMessage<String>) messageCollector.forChannel(processor.output()).poll();
        assertThat(result.getPayload()).isEqualTo(VALID_CACHE_PAYLOAD_ONE);

    }
}