package ca.utoronto.cs.message;

public class UnknownResponse extends Response {
	@Override
	protected MessageType getType() {
		return MessageType.UNKNOWN_RESPONSE;
	}

	public String content;
}
