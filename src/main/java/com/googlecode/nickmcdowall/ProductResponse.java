package com.googlecode.nickmcdowall;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize
public interface ProductResponse {
    String id();
    String colour();
    String size();
}
