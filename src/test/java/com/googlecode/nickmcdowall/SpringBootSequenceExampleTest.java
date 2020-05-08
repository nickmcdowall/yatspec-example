package com.googlecode.nickmcdowall;

import com.googlecode.nickmcdowall.product.ImmutableProductResponse;
import com.googlecode.nickmcdowall.product.ProductResponse;
import com.googlecode.nickmcdowall.stub.HttpServiceStubber;
import com.googlecode.yatspec.junit.SequenceDiagramExtension;
import com.googlecode.yatspec.junit.WithParticipants;
import com.googlecode.yatspec.sequence.Participant;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import com.googlecode.yatspec.state.givenwhenthen.WithTestState;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static com.googlecode.nickmcdowall.product.ImmutableProductResponse.aProductResponseWith;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ContextConfiguration(classes = Application.class, initializers = ConfigFileApplicationContextInitializer.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(SequenceDiagramExtension.class)
public class SpringBootSequenceExampleTest implements WithTestState, WithParticipants {

    @Value("${app.host}")
    private String appHost;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private HttpServiceStubber httpServiceStubber;

    @Autowired
    private TestState interactions;

    @Autowired
    private List<Participant> participants;

    private ProductResponse productResponse;

    @Test
    public void handleProductDetailsRequest() {
        givenALargeRedFrisbeeProductExistsWithId("5");

        whenAnApiRequestIsReceivedFor("/product/details/5");

        thenApiReturns(aProductResponseWith().id("5").size("large").colour("red").description("Frisbee"));
    }

    @AfterEach
    public void teardown() {
        httpServiceStubber.stop();
    }

    @Override
    public TestState testState() {
        return interactions;
    }

    @Override
    public List<Participant> participants() {
        return participants;
    }

    private void givenALargeRedFrisbeeProductExistsWithId(String id) {
        httpServiceStubber.stubGet("/colour/" + id, "responses/red.json", "application/json");
        httpServiceStubber.stubGet("/size/" + id, "responses/large.json", "application/json");
        httpServiceStubber.stubGet("/description/" + id, "responses/frisbee.json", "application/json");
    }

    private void whenAnApiRequestIsReceivedFor(final String path) {
        productResponse = testRestTemplate.getForObject(appHost + path, ProductResponse.class);
    }

    private void thenApiReturns(ImmutableProductResponse.Builder expectedResponse) {
        assertThat(productResponse).isEqualTo(expectedResponse.build());
    }

}
