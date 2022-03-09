package ca.utoronto.cs.message;

import ca.utoronto.cs.entity.Room;

public class CreateRoomResponse extends Response{
    @Override
    protected MessageType getType() {
        return MessageType.CREATE_ROOM_RESPONSE;
    }
    public Room room;
}
