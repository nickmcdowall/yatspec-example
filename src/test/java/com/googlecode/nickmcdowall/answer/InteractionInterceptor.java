package com.googlecode.nickmcdowall.answer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT;

public class InteractionInterceptor<T> implements Answer<T> {

    private ObjectMapper mapper = new ObjectMapper().enable(INDENT_OUTPUT);

    private final TestState interactions;
    private final String sourceName;
    private final String targetName;
    private final int urlArgumentIndex;

    public InteractionInterceptor(TestState interactions, String sourceName, String targetName, int urlArgumentIndex) {
        this.interactions = interactions;
        this.sourceName = sourceName;
        this.targetName = targetName;
        this.urlArgumentIndex = urlArgumentIndex;
    }

    @Override
    public T answer(InvocationOnMock invocation) throws Throwable {
        String url = (String) invocation.getArguments()[urlArgumentIndex];
        captureRequestArgument(url);
        T realResponse = (T) invocation.callRealMethod();
        captureJsonResponse(realResponse);
        return realResponse;
    }

    private void captureRequestArgument(String url) {
        interactions.log(url + " from " + sourceName + " to " + targetName, url);
    }

    private void captureJsonResponse(T realResponse) {
        interactions.log("response from " + targetName + " to " + sourceName, convertToJson(realResponse));
    }

    private String convertToJson(T realResponse) {
        try {
            return mapper.writeValueAsString(realResponse);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
