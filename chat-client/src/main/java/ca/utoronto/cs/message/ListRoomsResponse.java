package ca.utoronto.cs.message;

import ca.utoronto.cs.entity.Room;
import java.util.List;

public class ListRoomsResponse extends Response {
    @Override
    protected MessageType getType() { return MessageType.LIST_ROOMS_RESPONSE; }

    public List<Room> rooms;
}
