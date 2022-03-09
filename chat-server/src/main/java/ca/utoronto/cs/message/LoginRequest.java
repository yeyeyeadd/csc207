package ca.utoronto.cs.message;

public class LoginRequest extends Request {
	/**
	 * return a LOGIN_REQUEST, type of MessageType
	 * @return a LOGIN_REQUEST, type of MessageType
	 */
	@Override
	protected MessageType getType() { return MessageType.LOGIN_REQUEST; }

	public String username;
	public String password;
}
