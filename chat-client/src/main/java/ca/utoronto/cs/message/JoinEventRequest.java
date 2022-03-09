package ca.utoronto.cs.message;

public class JoinEventRequest extends Request{
	/**
	 * return a JOIN_EVENT_REQUEST, type of MessageType
	 * @return a JOIN_EVENT_REQUEST, type of MessageType
	 */
	@Override
	protected MessageType getType() { return MessageType.JOIN_EVENT_REQUEST; }

	public int eventId;
}
