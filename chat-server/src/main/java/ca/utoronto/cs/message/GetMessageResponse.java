package ca.utoronto.cs.message;

import ca.utoronto.cs.entity.Message;

import java.util.List;

public class GetMessageResponse extends Response{
    @Override
    protected MessageType getType() {
        return MessageType.GET_MESSAGE_RESPONSE;
    }
    public List<Message> messages;
}
