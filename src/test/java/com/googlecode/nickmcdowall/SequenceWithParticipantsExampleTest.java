package com.googlecode.nickmcdowall;

import com.googlecode.yatspec.junit.SequenceDiagramExtension;
import com.googlecode.yatspec.junit.WithParticipants;
import com.googlecode.yatspec.sequence.Participant;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static com.googlecode.yatspec.sequence.Participants.*;

@ExtendWith(SequenceDiagramExtension.class)
public class SequenceWithParticipantsExampleTest implements WithParticipants {

    private static final String NEW_USER_REQUEST = "{ \"name\" : \"nick\" }";
    private static final String EXPECTED_ENRICHED_MESSAGE = "<some-xml/>";
    private static final String LOOKUP_ID = "555";
    private static final String SERVICE_LOOKUP_RESPONSE = "{ \"555\" : \"ok\"} ";

    private TestState interactions = new TestState();

    @Test
    public void messageFromUpstreamToDownstream() {
        givenThatAnActorSendsAMessageToTopic();

        whenTheAppConsumesTheMessageAndPersistsIt();

        thenTheAppFetchesTheFullCollection();
        andAnEnrichedMessageIsSentToTheOutboundQueue();
    }

    private void givenThatAnActorSendsAMessageToTopic() {
        interactions.log("send message from User to inbound", NEW_USER_REQUEST);
    }

    private void whenTheAppConsumesTheMessageAndPersistsIt() {
        interactions.log("consume from App to inbound", NEW_USER_REQUEST);
        interactions.log("persist from App to repository", NEW_USER_REQUEST);
    }

    private void thenTheAppFetchesTheFullCollection() {
        interactions.log("GET /lookup/" + LOOKUP_ID + " from App to allMessages", "");
        interactions.log("response from allMessages to App", SERVICE_LOOKUP_RESPONSE);
    }

    private void andAnEnrichedMessageIsSentToTheOutboundQueue() {
        interactions.log("send enriched message from App to outbound", EXPECTED_ENRICHED_MESSAGE);
    }

    @Override
    public List<Participant> participants() {
        return List.of(
                ACTOR.create("User", "Jon from accounting"),
                CONTROL.create("inbound"),
                PARTICIPANT.create("App"),
                DATABASE.create("repository"),
                COLLECTIONS.create("allMessages", "message collection"),
                BOUNDARY.create("outbound")
        );
    }
}
