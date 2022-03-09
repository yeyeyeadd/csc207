package ca.utoronto.cs.message;

import ca.utoronto.cs.entity.Event;

public class ChangeEventCapacityResponse extends Response{
    @Override
    protected MessageType getType() {
        return MessageType.GET_MESSAGE_RESPONSE;
    }
    public Event event;
}
