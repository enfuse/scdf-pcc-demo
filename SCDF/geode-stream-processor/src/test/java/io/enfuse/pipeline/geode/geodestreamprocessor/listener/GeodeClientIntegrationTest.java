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

        exampleOne.setId(1L);
        exampleOne.setValue(EXAMPLE_ONE_VALUE);

        exampleTwo.setId(2L);
        exampleTwo.setValue(EXAMPLE_TWO_VALUE);

        exampleThree.setId(3L);
        exampleThree.setValue(EXAMPLE_THREE_VALUE);

        exampleFour.setId(4L);
        exampleFour.setValue(EXAMPLE_FOUR_VALUE);

        example = this.clientCache.getRegion("exampleRegion");

        example.put(1L, exampleOne);
        example.put(2L, exampleTwo);
        example.put(3L, exampleThree);
        example.put(4L, exampleFour);

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

        assertThat(example.get(1L).getValue()).isEqualTo("example one value");
        assertThat(example.get(2L).getValue()).isEqualTo("example two value");

    }

    @Test
    public void repositoryReturnsExpectedValuesFromClientCache() {
        String valueOneToCheck = exampleRepository.findById(1L)
                .orElse(new ExampleEntity()).getValue();
        assertThat(valueOneToCheck).isEqualTo(EXAMPLE_ONE_VALUE);

        String valueTwoToCheck = exampleRepository.findById(2L)
                .orElse(new ExampleEntity()).getValue();
        assertThat(valueTwoToCheck).isEqualTo(EXAMPLE_TWO_VALUE);

        String valueThreeToCheck = exampleRepository.findById(3L)
                .orElse(new ExampleEntity()).getValue();
        assertThat(valueThreeToCheck).isEqualTo(EXAMPLE_THREE_VALUE);
    }


    @Test
    public void streamListenerTakesMessageWithIdAndReturnsCachedValue() throws JSONException {

        String payloadOne = "{\"id\":\"1\", \"value\":\"foo\"}";
        String payloadTwo = "{\"id\":\"2\", \"value\":\"foo\"}";
        String payloadThree = "{\"id\":\"3\"}";


        this.processor.input().send(new GenericMessage<>(payloadOne));
        this.processor.input().send(new GenericMessage<>(payloadTwo));
        this.processor.input().send(new GenericMessage<>(payloadThree));
        BlockingQueue<Message<?>> messages = messageCollector.forChannel(processor.output());

        for (Message message : messages) {

            logger.info(message.toString());
        }
        // assertThat(messages, receivesPayloadThat(is()))
//        assertThat(messages, receivesPayloadThat(is(payloadOne)));
//        assertThat(messages, receivesPayloadThat(is(payloadTwo)));
    }

    @Test
    public void testOQLQueryOutput() {
        exampleRepository.findOneById(1L);
    }

}
