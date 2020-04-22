package com.googlecode.nickmcdowall.client;

import org.springframework.web.client.RestTemplate;

public class GenericClient<T> implements LookupClient<T> {

    private final RestTemplate restTemplate;
    private final Class<T> responseClass;
    private final String endpoint;

    public GenericClient(RestTemplate restTemplate, String endpointTemplate, Class<T> responseClass) {
        this.restTemplate = restTemplate;
        this.responseClass = responseClass;
        this.endpoint = endpointTemplate;
    }

    @Override
    public T lookup(String id) {
        return restTemplate.getForObject(String.format(endpoint, id), responseClass);
    }
}
