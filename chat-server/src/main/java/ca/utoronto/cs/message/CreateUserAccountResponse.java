package ca.utoronto.cs.message;

import ca.utoronto.cs.entity.User;

public class CreateUserAccountResponse extends Response{
    /**
     * return a CREATE_USER_ACCOUNT_RESPONSE, type of MessageType
     * @return a CREATE_USER_ACCOUNT_RESPONSE, type of MessageType
     */
    @Override
    protected MessageType getType() {
        return MessageType.CREATE_USER_ACCOUNT_RESPONSE;
    }

    public User user;
}
