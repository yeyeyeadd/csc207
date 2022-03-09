package ca.utoronto.cs.message;

import ca.utoronto.cs.entity.Event;

public class LeaveEventResponse extends Response{
    /**
     * return a LEAVE_EVENT_RESPONSE, type of MessageType
     * @return a LEAVE_EVENT_RESPONSE, type of MessageType
     */
    @Override
    protected MessageType getType() {
        return MessageType.LEAVE_EVENT_RESPONSE;
    }

    public Event event;
}
