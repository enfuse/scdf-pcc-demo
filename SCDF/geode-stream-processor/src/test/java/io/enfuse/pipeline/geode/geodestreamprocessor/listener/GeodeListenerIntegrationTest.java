package io.enfuse.pipeline.geode.geodestreamprocessor.listener;

import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.cloud.stream.test.binder.MessageCollector;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.BlockingQueue;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.springframework.cloud.stream.test.matcher.MessageQueueMatcher.receivesPayloadThat;

@RunWith(SpringRunner.class)
@SpringBootTest
//@EnableEntityDefinedRegions(clientRegionShortcut = ClientRegionShortcut.LOCAL)
@ActiveProfiles("integration")
public class GeodeListenerIntegrationTest {

    @Autowired
    private Processor processor;

    @Autowired
    private MessageCollector messageCollector;

    @Test
    public void handle_givenString_sendsString() throws JSONException {
        String payloadOne = "{\"value\":\"foo\"}";
        String payloadTwo = "{\"value\":\"bar\"}";


        this.processor.input().send(new GenericMessage<>(payloadOne));
        this.processor.input().send(new GenericMessage<>(payloadTwo));

        BlockingQueue<Message<?>> messages = messageCollector.forChannel(processor.output());

        assertThat(messages, receivesPayloadThat(is(payloadOne)));
        assertThat(messages, receivesPayloadThat(is(payloadTwo)));

    }
}
