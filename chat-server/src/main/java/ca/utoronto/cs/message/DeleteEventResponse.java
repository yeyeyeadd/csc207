package ca.utoronto.cs.message;

public class DeleteEventResponse extends Response{
    /**
     * return a DELETE_EVENT_RESPONSE, type of MessageType
     * @return a DELETE_EVENT_RESPONSE, type of MessageType
     */
    @Override
    protected MessageType getType() {
        return MessageType.DELETE_EVENT_RESPONSE;
    }
}
