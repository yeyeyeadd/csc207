package ca.utoronto.cs.message;

public class ChangeEventCapacityRequest extends Request {
    @Override
    protected MessageType getType() {
        return MessageType.CHANGE_EVENT_CAPACITY_REQUEST;
    }
    public int eventid;
    public int capacity;
}
