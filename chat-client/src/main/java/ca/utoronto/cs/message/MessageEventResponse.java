package ca.utoronto.cs.message;

import ca.utoronto.cs.entity.Message;

import java.util.List;

public class MessageEventResponse extends Response{
    /**
     *return  a MESSAGE_EVENT_RESPONSE, type of MessageType
     * @return  a MESSAGE_EVENT_RESPONSE, type of MessageType
     */
    @Override
    protected MessageType getType() {
        return MessageType.MESSAGE_EVENT_RESPONSE;
    }


    public List<Message> msgLst;
}
