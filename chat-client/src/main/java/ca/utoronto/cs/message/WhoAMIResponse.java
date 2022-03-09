package ca.utoronto.cs.message;

import ca.utoronto.cs.entity.User;

public class WhoAMIResponse extends Response {
    @Override
    protected MessageType getType() {
        return MessageType.WHO_AM_I_RESPONSE;
    }
    public User user;
}
