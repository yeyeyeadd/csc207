package ca.utoronto.cs.message;

public class MessageEventRequest extends Request{
    /**
     *return a MESSAGE_EVENT_REQUEST, type of MessageType
     * @return a MESSAGE_EVENT_REQUEST, type of MessageType
     */
    @Override
    protected MessageType getType() {
        return MessageType.MESSAGE_EVENT_REQUEST;
    }

    public int eventId;
    public String message;
}
