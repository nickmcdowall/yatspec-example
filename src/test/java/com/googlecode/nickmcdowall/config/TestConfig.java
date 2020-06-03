package com.googlecode.nickmcdowall.config;

import com.googlecode.nickmcdowall.stub.HttpServiceStubber;
import com.googlecode.yatspec.sequence.Participant;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static com.googlecode.yatspec.sequence.Participants.*;

@Configuration
public class TestConfig {

    @Bean
    public HttpServiceStubber httpServiceStubber(@Value("${wiremock.server.port}") int port) {
        return new HttpServiceStubber(port);
    }

    @Bean
    public TestState interactions() {
        return new TestState();
    }

    /**
     * Provide additional styling information for the sequence diagram - name or alias name to display, ordering and
     * entity type.
     */
    @Bean
    public List<Participant> participants() {
        return List.of(
                ACTOR.create("User"),
                PARTICIPANT.create("App"),
                COLLECTIONS.create("size", "SizeService"),
                COLLECTIONS.create("colour", "ColourService"),
                COLLECTIONS.create("description", "DescriptionService")
        );
    }

}
