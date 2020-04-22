package com.googlecode.nickmcdowall;

import com.googlecode.nickmcdowall.answer.InteractionInterceptor;
import com.googlecode.nickmcdowall.stub.HttpStubber;
import com.googlecode.yatspec.junit.SequenceDiagramExtension;
import com.googlecode.yatspec.junit.SpecListener;
import com.googlecode.yatspec.junit.WithParticipants;
import com.googlecode.yatspec.sequence.Participant;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import com.googlecode.yatspec.state.givenwhenthen.WithTestState;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static com.googlecode.yatspec.sequence.Participants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ContextConfiguration(classes = Application.class, initializers = ConfigFileApplicationContextInitializer.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith({SpecListener.class, SequenceDiagramExtension.class})
public class SpringBootSequenceExampleTest implements WithTestState, WithParticipants {

    @Autowired
    private TestRestTemplate restTemplate;

    @SpyBean(name = "sizeRestTemplate")
    private RestTemplate sizeRestTemplate;

    @SpyBean(name = "colourRestTemplate")
    private RestTemplate colourRestTemplate;

    @LocalServerPort
    private int port;

    private TestState interactions = new TestState();
    private HttpStubber httpStubber = new HttpStubber(8090);
    private InteractionInterceptor sizeClientInterceptor = new InteractionInterceptor(interactions, "App", "sizing", 0);
    private InteractionInterceptor colourClientInterceptor = new InteractionInterceptor(interactions, "App", "colouring", 0);
    private String serviceResponse;

    @BeforeEach
    public void setup() {
        doAnswer(sizeClientInterceptor).when(sizeRestTemplate).getForObject(anyString(), any());
        doAnswer(colourClientInterceptor).when(colourRestTemplate).getForObject(anyString(), any());
    }

    @AfterEach
    public void teardown() {
        httpStubber.stop();
    }

    @Test
    public void testStatusCodePositive() {
        givenAnExistingProductWithId("5");
        whenTheUserRequestsProductWithId("5");
        thenTheResponseContainsTheExpectedValues();
    }

    private void givenAnExistingProductWithId(String id) {
        httpStubber.stub("/colour/" + id, "json/colour_response.json");
        httpStubber.stub("/size/" + id, "json/size_response.json");
    }

    private void thenTheResponseContainsTheExpectedValues() {
        assertThat(serviceResponse)
                .contains("\"colour\": \"red\"")
                .contains("\"size\": \"large\"");
    }

    private void whenTheUserRequestsProductWithId(final String id) {
        String path = "/product/details/" + id;
        interactions.log("request product with id " + id + " from user to App", path);
        serviceResponse = restTemplate.getForObject("http://localhost:" + port + path, String.class);
        interactions.log("response from App to user", serviceResponse);
    }

    @Override
    public TestState testState() {
        return interactions;
    }

    @Override
    public List<Participant> participants() {
        return List.of(
                ACTOR.create("user", "Alice"),
                PARTICIPANT.create("App"),
                COLLECTIONS.create("colouring", "colour service"),
                COLLECTIONS.create("sizing", "sizing service")
        );
    }
}
