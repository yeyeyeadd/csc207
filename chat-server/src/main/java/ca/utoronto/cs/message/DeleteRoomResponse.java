package ca.utoronto.cs.message;



public class DeleteRoomResponse extends Response{
    @Override
    protected MessageType getType() {
        return MessageType.DELETE_ROOM_RESPONSE;
    }
}
