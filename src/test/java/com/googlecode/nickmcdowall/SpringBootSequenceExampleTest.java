package com.googlecode.nickmcdowall;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.googlecode.nickmcdowall.answer.InteractionInterceptor;
import com.googlecode.nickmcdowall.client.colour.ColourResponse;
import com.googlecode.nickmcdowall.client.description.DescriptionResponse;
import com.googlecode.nickmcdowall.client.size.SizeResponse;
import com.googlecode.nickmcdowall.product.ImmutableProductResponse;
import com.googlecode.nickmcdowall.product.ProductResponse;
import com.googlecode.nickmcdowall.stub.HttpServiceStubber;
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

import static com.googlecode.nickmcdowall.product.ImmutableProductResponse.aProductResponseWith;
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

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @SpyBean(name = "sizeRestTemplate")
    private RestTemplate sizeRestTemplate;

    @SpyBean(name = "colourRestTemplate")
    private RestTemplate colourRestTemplate;

    @SpyBean(name = "descriptionRestTemplate")
    private RestTemplate descriptionRestTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private final TestState interactions = new TestState();
    private final HttpServiceStubber httpServiceStubber = new HttpServiceStubber(8090);
    private final InteractionInterceptor<SizeResponse> sizeInterceptor = new InteractionInterceptor<>(interactions, "App", "sizing", 0);
    private final InteractionInterceptor<ColourResponse> colourInterceptor = new InteractionInterceptor<>(interactions, "App", "colouring", 0);
    private final InteractionInterceptor<DescriptionResponse> descriptionInterceptor = new InteractionInterceptor<>(interactions, "App", "description", 0);

    private ProductResponse productResponse;
    private String applicationHost;

    @BeforeEach
    public void setup() {
        applicationHost = "http://localhost:" + port;
        doAnswer(sizeInterceptor).when(sizeRestTemplate).getForObject(anyString(), any());
        doAnswer(colourInterceptor).when(colourRestTemplate).getForObject(anyString(), any());
        doAnswer(descriptionInterceptor).when(descriptionRestTemplate).getForObject(anyString(), any());
    }

    @Test
    public void handleProductDetailsRequest() throws JsonProcessingException {
        givenALargeRedFrizbeeProductExistsWithId("5");

        whenAnApiRequestIsReceivedFor("/product/details/5");

        thenSend(aProductResponseWith().id("5").size("large").colour("red").description("Frisbee"));
    }

    @AfterEach
    public void teardown() {
        httpServiceStubber.stop();
    }

    private void givenALargeRedFrizbeeProductExistsWithId(String id) {
        interactions.interestingGivens().add("productId", "5");
        httpServiceStubber.stub("/colour/" + id, "responses/5/red.json");
        httpServiceStubber.stub("/size/" + id, "responses/5/large.json");
        httpServiceStubber.stub("/description/" + id, "responses/5/frizbee.json");
    }

    private void whenAnApiRequestIsReceivedFor(final String path) throws JsonProcessingException {
        captureInboundRequest(path);
        productResponse = restTemplate.getForObject(applicationHost + path, ProductResponse.class);
        captureResponse(productResponse);
    }

    private void thenSend(ImmutableProductResponse.Builder expectedResponse) {
        assertThat(productResponse).isEqualTo(expectedResponse.build());
    }

    @Override
    public TestState testState() {
        return interactions;
    }

    @Override
    public List<Participant> participants() {
        return List.of(
                ACTOR.create("user", "Customer"),
                PARTICIPANT.create("App", "Product Lookup"),
                COLLECTIONS.create("colouring", "colour service"),
                COLLECTIONS.create("sizing", "sizing service"),
                COLLECTIONS.create("description", "description service")
        );
    }

    private void captureResponse(ProductResponse serviceResponse) throws JsonProcessingException {
        interactions.log("response from App to user", objectMapper.writeValueAsString(serviceResponse));
    }

    private void captureInboundRequest(String path) {
        interactions.log("GET " + path + " from user to App", path);
    }
}
