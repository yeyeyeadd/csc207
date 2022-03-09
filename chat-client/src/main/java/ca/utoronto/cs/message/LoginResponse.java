package ca.utoronto.cs.message;

import ca.utoronto.cs.entity.User;

public class LoginResponse extends Response {
	/**
	 *return a LOGIN_RESPONSE, type of MessageType
	 * @return a LOGIN_RESPONSE, type of MessageType
	 */
	@Override
	protected MessageType getType() {
		return MessageType.LOGIN_RESPONSE;
	}

	public User user;
	public String message;

	/**
	 *return the String type of response.
	 * @return the String type of response.
	 */
	@Override
	public String toString() {
		return String.format("LoginResponse: user=%s", user);
	}
}
