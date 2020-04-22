package com.googlecode.nickmcdowall.answer;

import com.googlecode.yatspec.state.givenwhenthen.TestState;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class InteractionInterceptor implements Answer {

    private final TestState interactions;
    private final String sourceName;
    private final String targetName;
    private final int argumentIndex;

    public InteractionInterceptor(TestState interactions, String sourceName, String targetName, int argumentIndex) {
        this.interactions = interactions;
        this.sourceName = sourceName;
        this.targetName = targetName;
        this.argumentIndex = argumentIndex;
    }

    @Override
    public Object answer(InvocationOnMock invocation) throws Throwable {
        Object capturedArgument = invocation.getArguments()[argumentIndex];
        interactions.log("call from " + sourceName + " to " + targetName, capturedArgument);
        Object realResponse = invocation.callRealMethod();
        interactions.log("response from " + targetName + " to " + sourceName, realResponse);
        return realResponse;
    }

}
