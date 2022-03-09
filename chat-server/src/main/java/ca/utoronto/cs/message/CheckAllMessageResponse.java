package ca.utoronto.cs.message;

import ca.utoronto.cs.entity.Message;

import java.util.List;

public class CheckAllMessageResponse extends Response{
    /**
     * return a CHECK_ALL_MESSAGE_RESPONSE, type of MessageType
     * @return a CHECK_ALL_MESSAGE_RESPONSE, type of MessageType
     */
    @Override
    protected MessageType getType() {
        return MessageType.CHECK_ALL_MESSAGE_RESPONSE;
    }

    public List<Message> messages;
}
