package com.googlecode.nickmcdowall;

import com.googlecode.nickmcdowall.client.ColourResponse;
import com.googlecode.nickmcdowall.client.LookupClient;
import com.googlecode.nickmcdowall.client.SizeResponse;

public class AppService {

    private final LookupClient<ColourResponse> colourClient;
    private final LookupClient<SizeResponse> sizeClient;

    public AppService(LookupClient<ColourResponse> colourClient, LookupClient<SizeResponse> sizeClient) {
        this.colourClient = colourClient;
        this.sizeClient = sizeClient;
    }

    public ProductResponse getProductDetailsFor(String id) {
        return ImmutableProductResponse.builder()
                .colour(colourClient.lookup(id).colour())
                .size(sizeClient.lookup(id).size()).id(id)
                .build();
    }
}
