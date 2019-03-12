package io.enfuse.pipeline.geode.geodestreamprocessor.listener;

import io.enfuse.pipeline.geode.geodestreamprocessor.domain.ExampleEntity;
import io.enfuse.pipeline.geode.geodestreamprocessor.domain.ExampleRepository;
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
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.gemfire.util.RegionUtils;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;
import java.util.concurrent.BlockingQueue;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ActiveProfiles("integration")
@SpringBootTest
@ComponentScan("io.enfuse.pipeline.geode.geodestreamprocessor")

public class GeodeClientIntegrationTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ExampleRepository exampleRepository;

    @Autowired
    ClientCache clientCache;

    @Autowired
    private Processor processor;

    @Autowired
    private MessageCollector messageCollector;

    private final static String EXAMPLE_ONE_VALUE = "example one value";
    private final static String EXAMPLE_TWO_VALUE = "example two value";
    private final static String EXAMPLE_THREE_VALUE = "example three value";
    private final static String EXAMPLE_FOUR_VALUE = "example four value";

    private Region<Object, ExampleEntity> example;

    @Before
    public void setup() {
        ExampleEntity exampleOne = new ExampleEntity();
        ExampleEntity exampleTwo = new ExampleEntity();
        ExampleEntity exampleThree = new ExampleEntity();
        ExampleEntity exampleFour = new ExampleEntity();

        exampleOne.setId("1");
        exampleOne.setValue(EXAMPLE_ONE_VALUE);

        exampleTwo.setId("2");
        exampleTwo.setValue(EXAMPLE_TWO_VALUE);

        exampleThree.setId("3");
        exampleThree.setValue(EXAMPLE_THREE_VALUE);

        exampleFour.setId("4");
        exampleFour.setValue(EXAMPLE_FOUR_VALUE);

        example = this.clientCache.getRegion("exampleRegion");

        example.put("1", exampleOne);
        example.put("2", exampleTwo);
        example.put("3", exampleThree);
        example.put("4", exampleFour);

    }

    @Test
    public void clientCacheAndClientRegionAreAvailable() {
        Optional.ofNullable(clientCache)
                .filter(it -> it instanceof GemFireCacheImpl)
                .map(it -> (GemFireCacheImpl) it)
                .map(it -> assertThat(it.isClient()).isTrue())
                .orElseThrow(() -> new IllegalStateException("ClientCache was null"));

        assertThat(example).isNotNull();
        assertThat(example.getName()).isEqualTo("exampleRegion");
        assertThat(example.getFullPath()).isEqualTo(RegionUtils.toRegionPath("exampleRegion"));
    }

    @Test
    public void clientCacheContainsSetupData() {
        assertThat(example).isNotNull();
        assertThat(example.getName()).isEqualTo("exampleRegion");
        assertThat(example.getFullPath()).isEqualTo(RegionUtils.toRegionPath("exampleRegion"));
        assertThat(example.get("1").getValue()).isEqualTo("example one value");
        assertThat(example.get("2").getValue()).isEqualTo("example two value");
    }

    @Test
    public void streamListenerTakesMessageWithIdAndReturnsCachedValue() throws JSONException {
        String payloadOne = "{\"id\":\"1\"}";
        String payloadTwo = "{\"id\":\"2\"}";
        String payloadThree = "{\"id\":\"3\"}";

        this.processor.input().send(new GenericMessage<>(payloadOne));
        this.processor.input().send(new GenericMessage<>(payloadTwo));
        this.processor.input().send(new GenericMessage<>(payloadThree));
        BlockingQueue<Message<?>> messages = messageCollector.forChannel(processor.output());

        for (Message message : messages) {

            logger.info(message.toString());
        }
    }

}
