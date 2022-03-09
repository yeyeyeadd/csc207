package ca.utoronto.cs.message;

import ca.utoronto.cs.entity.User;

public class AddFriendResponse extends Response{
    /**
     * return a ADD_FRIEND_RESPONSE
     * @return a ADD_FRIEND_RESPONSE, type of MessageType
     */
    @Override
    protected MessageType getType() {
        return MessageType.ADD_FRIEND_RESPONSE;
    }
    public User userA;
    public User userB;
}
