package com.googlecode.nickmcdowall;

import com.googlecode.nickmcdowall.client.LookupClient;

public class AppService {

    private final LookupClient lookupClient;
    private final LookupClient sizeLookupClient;

    public AppService(LookupClient lookupClient, LookupClient sizeLookupClient) {
        this.lookupClient = lookupClient;
        this.sizeLookupClient = sizeLookupClient;
    }

    public String getDetailsFor(String id) {
        String colour = lookupClient.lookup(id);
        String size = sizeLookupClient.lookup(id);

        return "{" +
                "\"colour\": " + colour +
                ", \"size\": " + size +
                "}";
    }
}
