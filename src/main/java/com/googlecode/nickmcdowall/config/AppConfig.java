package com.googlecode.nickmcdowall.config;

import com.googlecode.nickmcdowall.AppService;
import com.googlecode.nickmcdowall.client.ColourResponse;
import com.googlecode.nickmcdowall.client.GenericClient;
import com.googlecode.nickmcdowall.client.LookupClient;
import com.googlecode.nickmcdowall.client.SizeResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Value("${lookup.colour.host}")
    private String colourHost;

    @Value("${lookup.size.host}")
    private String sizeHost;

    @Value("${lookup.colour.pathTemplate}")
    private String colourPathTemplate;

    @Value("${lookup.size.pathTemplate}")
    private String sizePathTemplate;

    @Bean
    public RestTemplate sizeRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    public RestTemplate colourRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    public LookupClient colourLookupClient() {
        return new GenericClient(colourRestTemplate(), colourHost + colourPathTemplate, ColourResponse.class);
    }

    @Bean
    public LookupClient sizeLookupClient() {
        return new GenericClient(sizeRestTemplate(), sizeHost + sizePathTemplate, SizeResponse.class);
    }

    @Bean
    public AppService productDetailsService() {
        return new AppService(colourLookupClient(), sizeLookupClient());
    }
}
