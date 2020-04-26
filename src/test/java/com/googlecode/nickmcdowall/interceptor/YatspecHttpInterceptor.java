package com.googlecode.nickmcdowall.interceptor;

import com.googlecode.yatspec.state.givenwhenthen.TestState;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.util.Map;

import static java.nio.charset.Charset.defaultCharset;
import static org.springframework.util.StreamUtils.copyToString;

public class YatspecHttpInterceptor implements ClientHttpRequestInterceptor {

    private final TestState interactions;
    private final String sourceName;
    private final Map<String, String> destinationMapping;

    public YatspecHttpInterceptor(TestState interactions, String sourceName, Map<String, String> destinationMapping) {
        this.interactions = interactions;
        this.destinationMapping = destinationMapping;
        this.sourceName = sourceName;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        String path = request.getURI().getPath();
        String destinationName = determineDestinationName(path);
        interactions.log(request.getMethodValue() + " " + path + " from " + sourceName + " to " + destinationName, body);
        ClientHttpResponse response = execution.execute(request, body);
        interactions.log("response code " + response.getRawStatusCode() + " from " + destinationName + " to " + sourceName, copyToString(response.getBody(), defaultCharset()));
        return response;
    }

    private String determineDestinationName(String path) {
        return destinationMapping.getOrDefault(destinationNameKey(path), "Other");
    }

    private String destinationNameKey(String path) {
        return destinationMapping.keySet().stream()
                .filter(key -> path.startsWith(key))
                .findFirst()
                .orElse("");
    }
}
