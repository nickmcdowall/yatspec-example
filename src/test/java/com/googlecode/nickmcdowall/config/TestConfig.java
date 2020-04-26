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
                ACTOR.create("User", "Customer"),
                PARTICIPANT.create("App", "ProductService"),
                COLLECTIONS.create("SizeService"),
                COLLECTIONS.create("ColourService"),
                COLLECTIONS.create("DescriptionService")
        );
    }

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    TestRestTemplate testRestTemplate;

    @PostConstruct
    public void configureYatspecInterceptor() {
        addInterceptor(restTemplate, "App", Map.of(
                "/colour", "ColourService",
                "/size", "SizeService",
                "/description", "DescriptionService"));

        addInterceptor(testRestTemplate.getRestTemplate(), "User", Map.of("/", "App"));
    }

    private void addInterceptor(RestTemplate restTemplate, String sourceName, Map<String, String> targetNameMapping) {
        restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));
        restTemplate.setInterceptors(List.of(
                new YatspecHttpInterceptor(interactions(), sourceName, targetNameMapping)
        ));
    }

}
