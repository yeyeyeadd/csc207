package ca.utoronto.cs.message;

public class DeleteEventRequest extends Request{
    /**
     * return a DELETE_EVENT_REQUEST, type of MessageType
     * @return a DELETE_EVENT_REQUEST, type of MessageType
     */
    @Override
    protected MessageType getType() {
        return MessageType.DELETE_EVENT_REQUEST;
    }

    public int eventId;
}
