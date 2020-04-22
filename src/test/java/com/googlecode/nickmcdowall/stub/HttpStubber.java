package com.googlecode.nickmcdowall.stub;

import com.github.tomakehurst.wiremock.WireMockServer;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class HttpStubber {

    private WireMockServer wireMockServer;

    public HttpStubber(int port) {
        wireMockServer = new WireMockServer(port);
        wireMockServer.start();
    }

    public void stub(String path, String file) {
        wireMockServer.stubFor(get(urlEqualTo(path))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBodyFile(file)));
    }

    public void stop() {
        wireMockServer.stop();
    }
}
