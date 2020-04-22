package com.googlecode.nickmcdowall.client;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonDeserialize(as = ImmutableColourResponse.class)
public interface ColourResponse {
    String id();

    String colour();
}
