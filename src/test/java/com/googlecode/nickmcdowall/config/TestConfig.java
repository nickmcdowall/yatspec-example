package com.googlecode.nickmcdowall.config;

import com.googlecode.nickmcdowall.interceptor.YatspecHttpInterceptor;
import com.googlecode.nickmcdowall.stub.HttpServiceStubber;
import com.googlecode.yatspec.sequence.Participant;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

import static com.googlecode.yatspec.sequence.Participants.*;

@Configuration
public class TestConfig {

    private static final String USER = "User";
    private static final String APP = "App";
    private static final String COLOUR_SERVICE = "ColourService";
    private static final String DESCRIPTION_SERVICE = "DescriptionService";
    private static final String SIZE_SERVICE = "SizeService";


    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Bean
    public HttpServiceStubber httpServiceStubber(@Value("${wiremock.server.port}") int port) {
        return new HttpServiceStubber(port);
    }

    @Bean
    public TestState interactions() {
        return new TestState();
    }

    @Bean
    public List<Participant> participants() {
        return List.of(
                ACTOR.create(USER),
                PARTICIPANT.create(APP, "ProductService"),
                COLLECTIONS.create(SIZE_SERVICE),
                COLLECTIONS.create(COLOUR_SERVICE),
                COLLECTIONS.create(DESCRIPTION_SERVICE)
        );
    }

    @PostConstruct
    public void configureYatspecInterceptor() {
        addInterceptor(restTemplate, APP, Map.of(
                "/colour", COLOUR_SERVICE,
                "/size", SIZE_SERVICE,
                "/description", DESCRIPTION_SERVICE));

        addInterceptor(testRestTemplate.getRestTemplate(), USER, Map.of("/", APP));
    }

    private void addInterceptor(RestTemplate restTemplate, String sourceName, Map<String, String> targetNameMapping) {
        restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));
        restTemplate.setInterceptors(List.of(
                new YatspecHttpInterceptor(interactions(), sourceName, targetNameMapping)
        ));
    }

}
