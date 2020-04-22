package com.googlecode.nickmcdowall.client;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonDeserialize(as = ImmutableSizeResponse.class)
public interface SizeResponse {
    String id();

    String size();
}
