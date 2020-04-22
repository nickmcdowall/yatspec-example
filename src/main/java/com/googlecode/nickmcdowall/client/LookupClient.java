package com.googlecode.nickmcdowall.client;

public interface LookupClient<T> {
    T lookup(String id);
}
