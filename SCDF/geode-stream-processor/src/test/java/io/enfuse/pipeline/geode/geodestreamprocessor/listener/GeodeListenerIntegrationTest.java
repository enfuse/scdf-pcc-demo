package io.enfuse.pipeline.geode.geodestreamprocessor.listener;

import io.enfuse.pipeline.geode.geodestreamprocessor.domain.ExampleEntity;
import io.enfuse.pipeline.geode.geodestreamprocessor.domain.ExampleRepository;
import org.apache.geode.cache.GemFireCache;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.apache.geode.internal.cache.GemFireCacheImpl;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.cloud.stream.test.binder.MessageCollector;
import org.springframework.context.annotation.Bean;
import org.springframework.data.gemfire.client.ClientRegionFactoryBean;
import org.springframework.data.gemfire.config.annotation.EnableLogging;
import org.springframework.data.gemfire.util.RegionUtils;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;
import java.util.concurrent.BlockingQueue;

//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.hamcrest.core.Is.is;
//import static org.springframework.cloud.stream.test.matcher.MessageQueueMatcher.receivesPayloadThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.data.gemfire.util.RuntimeExceptionFactory.newIllegalStateException;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("integration")
public class GeodeListenerIntegrationTest {

    private static final String GEMFIRE_LOG_LEVEL = "error";

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

        exampleOne.setId(1L);
        exampleOne.setValue("example one value");

        exampleTwo.setId(2L);
        exampleTwo.setValue("example two value");

        exampleThree.setId(3L);
        exampleThree.setValue("example three value");

        exampleFour.setId(4L);
        exampleFour.setValue("example four value");


        example.put(1L, exampleOne);
        example.put(2L, exampleTwo);
        example.put(3L, exampleThree);
        example.put(4L, exampleFour);
    }

    @Test
    public void handle_givenString_sendsString() throws JSONException {
        String payloadOne = "{\"value\":\"foo\"}";
        String payloadTwo = "{\"value\":\"bar\"}";


        this.processor.input().send(new GenericMessage<>(payloadOne));
        this.processor.input().send(new GenericMessage<>(payloadTwo));

        BlockingQueue<Message<?>> messages = messageCollector.forChannel(processor.output());

        for(Message message : messages) {
            System.out.println(message.getPayload());
        }

//        assertThat(messages, receivesPayloadThat(is(payloadOne)));
//        assertThat(messages, receivesPayloadThat(is(payloadTwo)));

    }

    @SpringBootApplication
    @EnableLogging(logLevel = GEMFIRE_LOG_LEVEL)
    static class TestConfiguration {

        @Bean("exampleRegion")
        public ClientRegionFactoryBean<Object, Object> exampleRegion(GemFireCache gemfireCache) {

            ClientRegionFactoryBean<Object, Object> clientRegion = new ClientRegionFactoryBean<>();

            clientRegion.setCache(gemfireCache);
            clientRegion.setClose(false);
            clientRegion.setShortcut(ClientRegionShortcut.LOCAL);

            return clientRegion;
        }
    }
}
