package ca.utoronto.cs.message;

public class LogoutRequest extends Request{
	/**
	 *return a LOGOUT_REQUEST, type of MessageType
	 * @return a LOGOUT_REQUEST, type of MessageType
	 */
	@Override
	protected MessageType getType() { return MessageType.LOGOUT_REQUEST; }
}
