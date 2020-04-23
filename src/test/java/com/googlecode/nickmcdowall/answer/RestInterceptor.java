package com.googlecode.nickmcdowall.answer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT;

public class RestInterceptor<T> implements Answer<T> {

    public static final int URL_INDEX = 0;

    private final ObjectMapper mapper = new ObjectMapper().enable(INDENT_OUTPUT);

    private final TestState interactions;
    private final String sourceName;
    private final String targetName;

    public RestInterceptor(TestState interactions, String sourceName, String targetName) {
        this.interactions = interactions;
        this.sourceName = sourceName;
        this.targetName = targetName;
    }

    @Override
    public T answer(InvocationOnMock invocation) throws Throwable {
        String url = invocation.getArgument(URL_INDEX);
        captureRequest(url);
        T realResponse = (T) invocation.callRealMethod();
        captureJsonString(realResponse);
        return realResponse;
    }

    private void captureRequest(String url) {
        interactions.log(url + " from " + sourceName + " to " + targetName, url);
    }

    private void captureJsonString(T realResponse) {
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
