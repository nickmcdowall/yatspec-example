import com.googlecode.yatspec.junit.SequenceDiagramExtension;
import com.googlecode.yatspec.junit.SpecListener;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import com.googlecode.yatspec.state.givenwhenthen.WithTestState;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({SpecListener.class, SequenceDiagramExtension.class})
public class SequenceDiagramExampleTest implements WithTestState {

    private static final String NEW_USER_REQUEST = "{ \"name\" : \"nick\" }";
    private static final String EXPECTED_ENRICHED_MESSAGE = "<some-xml/>";
    private static final String LOOKUP_ID = "555";
    private static final String SERVICE_LOOKUP_RESPONSE = "{ \"555\" : \"ok\"} ";

    private TestState interactions = new TestState();

    @Test
    public void messageFromUpstreamToDownstream() {
        givenThatUpstreamHasPublishedA(NEW_USER_REQUEST);

        whenTheAppConsumesTheMessageContainingThe(NEW_USER_REQUEST);

        thenTheAppFetchesTheEnrichmentDataUsingThe(LOOKUP_ID);
        andAnEnrichedMessageIsSentToTheOutboundTopicContainingThe(EXPECTED_ENRICHED_MESSAGE);
    }

    private void givenThatUpstreamHasPublishedA(String message) {
        interactions.log("message from Upstream to InboundTopic", message);

        interactions.interestingGivens
                .add("lookupId", LOOKUP_ID);
    }

    private void whenTheAppConsumesTheMessageContainingThe(String message) {
        interactions.log("subscription from App to InboundTopic", message);
    }

    private void thenTheAppFetchesTheEnrichmentDataUsingThe(String id) {
        interactions.log("call from App to ThirdPartyService", "/lookup/" + id);
        interactions.log("response from ThirdPartyService to App", SERVICE_LOOKUP_RESPONSE);
    }

    private void andAnEnrichedMessageIsSentToTheOutboundTopicContainingThe(String expectedOutputMessage) {
        interactions.log("EnrichedMessage from App to OutboundTopic", expectedOutputMessage);
    }

    @Override
    public TestState testState() {
        return interactions;
    }
}
