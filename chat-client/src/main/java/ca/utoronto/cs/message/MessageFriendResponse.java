package ca.utoronto.cs.message;

import ca.utoronto.cs.entity.Message;

public class MessageFriendResponse extends Response {
    /**
     *return a MESSAGE_FRIEND_RESPONSE, type of MessageType
     * @return a MESSAGE_FRIEND_RESPONSE, type of MessageType
     */
    @Override
    protected MessageType getType() {
        return MessageType.MESSAGE_FRIEND_RESPONSE;
    }


    public Message msgObj;
}
