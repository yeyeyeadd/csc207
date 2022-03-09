package ca.utoronto.cs.message;

import ca.utoronto.cs.entity.Role;

public class CreateUserAccountRequest extends Request{
    /**
     * return a CREATE_USER_ACCOUNT_REQUEST, type of MessageType
     * @return a CREATE_USER_ACCOUNT_REQUEST, type of MessageType
     */
    @Override
    protected MessageType getType() {
        return MessageType.CREATE_USER_ACCOUNT_REQUEST;
    }

    public String username;
    public String password;
    public Role role;
}
