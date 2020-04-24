package com.googlecode.nickmcdowall.product;

import com.googlecode.nickmcdowall.client.LookupClient;
import com.googlecode.nickmcdowall.client.colour.ColourResponse;
import com.googlecode.nickmcdowall.client.description.DescriptionResponse;
import com.googlecode.nickmcdowall.client.size.SizeResponse;

import static com.googlecode.nickmcdowall.product.ImmutableProductResponse.aProductResponseWith;

public class ProductService {

    private final LookupClient<ColourResponse> colourClient;
    private final LookupClient<SizeResponse> sizeClient;
    private final LookupClient<DescriptionResponse> descriptionResponse;

    public ProductService(LookupClient<ColourResponse> colourClient, LookupClient<SizeResponse> sizeClient, LookupClient<DescriptionResponse> descriptionResponse) {
        this.colourClient = colourClient;
        this.sizeClient = sizeClient;
        this.descriptionResponse = descriptionResponse;
    }

    public ProductResponse getProductDetailsFor(String id) {
        return aProductResponseWith()
                .id(id)
                .size(sizeClient.lookup(id).size())
                .colour(colourClient.lookup(id).colour())
                .description(descriptionResponse.lookup(id).description())
                .build();
    }
}
