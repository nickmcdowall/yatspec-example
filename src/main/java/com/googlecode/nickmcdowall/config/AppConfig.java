package com.googlecode.nickmcdowall.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.googlecode.nickmcdowall.client.GenericLookupClient;
import com.googlecode.nickmcdowall.client.LookupClient;
import com.googlecode.nickmcdowall.client.colour.ColourResponse;
import com.googlecode.nickmcdowall.client.description.DescriptionResponse;
import com.googlecode.nickmcdowall.client.size.SizeResponse;
import com.googlecode.nickmcdowall.product.ProductService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import static com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT;

@Configuration
public class AppConfig {

    @Value("${lookup.colour.endpoint}")
    private String colourEndpoint;

    @Value("${lookup.size.endpoint}")
    private String sizeEndpoint;

    @Value("${lookup.description.endpoint}")
    private String descriptionEndpoint;

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper().enable(INDENT_OUTPUT);
    }

    @Bean
    public RestTemplate sizeRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    public RestTemplate descriptionRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    public RestTemplate colourRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ProductService productDetailsService() {
        return new ProductService(
                colourLookupClient(),
                sizeLookupClient(),
                descriptionLookupClient()
        );
    }

    public LookupClient<ColourResponse> colourLookupClient() {
        return new GenericLookupClient<>(colourRestTemplate(), colourEndpoint, ColourResponse.class);
    }

    public LookupClient<SizeResponse> sizeLookupClient() {
        return new GenericLookupClient<>(sizeRestTemplate(), sizeEndpoint, SizeResponse.class);
    }

    public LookupClient<DescriptionResponse> descriptionLookupClient() {
        return new GenericLookupClient<>(descriptionRestTemplate(), descriptionEndpoint, DescriptionResponse.class);
    }
}
