package ca.utoronto.cs.message;

public class CreateRoomRequest extends Request{

    @Override
    protected MessageType getType() {
        return MessageType.CREATE_ROOM_REQUEST;
    }

    public int id;
    public String name;
    public int capacity;
}
