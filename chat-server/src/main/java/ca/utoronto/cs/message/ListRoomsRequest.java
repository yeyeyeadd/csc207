package ca.utoronto.cs.message;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
public class ListRoomsRequest extends Request{

    @Override
    protected MessageType getType() { return MessageType.LIST_ROOMS_REQUEST; }

}
