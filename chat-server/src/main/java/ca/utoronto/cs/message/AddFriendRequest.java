package ca.utoronto.cs.message;

public class AddFriendRequest extends Request{
    /**
     * return a ADD_FRIEND_REQUEST;
     * @return a ADD_FRIEND_REQUEST;
     */
    @Override
    protected MessageType getType() {
        return MessageType.ADD_FRIEND_REQUEST;
    }

    public int friendId;
}
