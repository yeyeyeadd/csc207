package ca.utoronto.cs.message;

public class MessageFriendRequest extends Request{
    /**
     *return a MESSAGE_FRIEND_REQUEST, type of MessageType
     * @return a MESSAGE_FRIEND_REQUEST, type of MessageType
     */
    @Override
    protected MessageType getType() {
        return MessageType.MESSAGE_FRIEND_REQUEST;
    }

    public String receiver;
    public String msg;
}
