package com.googlecode.nickmcdowall.client.size;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonDeserialize(as = ImmutableSizeResponse.class)
public interface SizeResponse {
    String size();
}
