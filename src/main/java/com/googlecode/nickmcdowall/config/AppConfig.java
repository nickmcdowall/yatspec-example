package com.googlecode.nickmcdowall.config;

import com.googlecode.nickmcdowall.client.LookupClient;
import com.googlecode.nickmcdowall.AppService;
import com.googlecode.nickmcdowall.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Value("${lookup.colour.host}")
    private String colourLookupHost;

    @Value("${lookup.size.host}")
    private String sizeLookupHost;

    @Value("${lookup.colour.pathTemplate}")
    private String colourLookupPathTemplate;

    @Value("${lookup.size.pathTemplate}")
    private String sizeLookupPathTemplate;

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
        return new RestClient(colourRestTemplate(), colourLookupHost, colourLookupPathTemplate);
    }

    @Bean
    public LookupClient sizeLookupClient() {
        return new RestClient(sizeRestTemplate(), sizeLookupHost, sizeLookupPathTemplate);
    }

    @Bean
    public AppService productDetailsService() {
        return new AppService(colourLookupClient(), sizeLookupClient());
    }
}
