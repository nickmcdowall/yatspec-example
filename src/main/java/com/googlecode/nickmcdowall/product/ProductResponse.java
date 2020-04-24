package com.googlecode.nickmcdowall.product;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

@Value.Immutable
@Value.Style(
        builder = "aProductResponseWith"
)
@JsonDeserialize(as = ImmutableProductResponse.class)
public interface ProductResponse {
    String id();

    String size();

    String colour();

    String description();
}
