package com.googlecode.nickmcdowall.config;

import com.googlecode.nickmcdowall.answer.RestInterceptor;
import com.googlecode.nickmcdowall.client.colour.ColourResponse;
import com.googlecode.nickmcdowall.client.description.DescriptionResponse;
import com.googlecode.nickmcdowall.client.size.SizeResponse;
import com.googlecode.nickmcdowall.stub.HttpServiceStubber;
import com.googlecode.yatspec.sequence.Participant;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static com.googlecode.yatspec.sequence.Participants.*;
import static com.googlecode.yatspec.sequence.Participants.COLLECTIONS;

@Configuration
public class TestConfig {

    @Bean
    public HttpServiceStubber httpServiceStubber(
            @Value("${wiremock.server.port}") int port) {
        return new HttpServiceStubber(port);
    }

    @Bean
    public TestState interactions() {
        return new TestState();
    }

    @Bean
    public RestInterceptor<ColourResponse> colourInterceptor() {
        return new RestInterceptor<>(interactions(), "App", "colouring");
    }

    @Bean
    public RestInterceptor<SizeResponse> sizeInterceptor() {
        return new RestInterceptor<>(interactions(), "App", "sizing");
    }

    @Bean
    public RestInterceptor<DescriptionResponse> descriptionInterceptor() {
        return new RestInterceptor<>(interactions(), "App", "description");
    }

    @Bean
    public List<Participant> participants() {
        return List.of(
                ACTOR.create("user", "Customer"),
                PARTICIPANT.create("App", "Product Lookup"),
                COLLECTIONS.create("colouring", "colour service"),
                COLLECTIONS.create("sizing", "sizing service"),
                COLLECTIONS.create("description", "description service")
        );
    }

}
