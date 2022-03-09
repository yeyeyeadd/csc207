package ca.utoronto.cs.message;

public class UnknownRequest extends Request {
	@Override
	protected MessageType getType() {
		return MessageType.UNKNOWN_REQUEST;
	}

	public String content;
}
