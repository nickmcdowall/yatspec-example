package com.googlecode.nickmcdowall.client.description;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonDeserialize(as = ImmutableDescriptionResponse.class)
public interface DescriptionResponse {
    String id();

    String description();
}
