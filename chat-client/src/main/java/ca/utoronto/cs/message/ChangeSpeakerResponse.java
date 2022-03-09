package ca.utoronto.cs.message;

import ca.utoronto.cs.entity.Event;

public class ChangeSpeakerResponse extends Response{
    /**
     * return a DELETE_EVENT_RESPONSE, type of MessageType
     * @return a DELETE_EVENT_RESPONSE, type of MessageType
     */
    @Override
    protected MessageType getType() {
        return MessageType.CHANGE_SPEAKER_RESPONSE;
    }

    public Event event;
}

