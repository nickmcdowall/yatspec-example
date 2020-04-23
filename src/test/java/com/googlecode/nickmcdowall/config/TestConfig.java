package com.googlecode.nickmcdowall.config;

import com.googlecode.nickmcdowall.stub.HttpServiceStubber;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfig {

    @Bean
    public HttpServiceStubber httpServiceStubber() {
        return new HttpServiceStubber(8090);
    }
}
