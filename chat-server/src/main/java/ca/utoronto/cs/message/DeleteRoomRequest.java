package ca.utoronto.cs.message;

public class DeleteRoomRequest extends Request{
    @Override
    protected MessageType getType() {
        return MessageType.DELETE_ROOM_REQUEST;
    }

    public int roomId;
}
