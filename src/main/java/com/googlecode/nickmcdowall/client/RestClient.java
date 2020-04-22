package com.googlecode.nickmcdowall.client;

import com.googlecode.nickmcdowall.client.LookupClient;
import org.springframework.web.client.RestTemplate;

public class RestClient implements LookupClient {

    private final RestTemplate restTemplate;
    private final String endpoint;

    public RestClient(RestTemplate restTemplate, String host, String path) {
        this.restTemplate = restTemplate;
        this.endpoint = host + path;
    }

    @Override
    public String lookup(String id) {
        return restTemplate.getForObject(String.format(endpoint, id), String.class);
    }
}
