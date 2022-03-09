package ca.utoronto.cs.message;

public class LeaveEventRequest extends Request {
	/**
	 * return a LEAVE_EVENT_REQUEST, type of MessageType
	 * @return a LEAVE_EVENT_REQUEST, type of MessageType
	 */
	@Override
	protected MessageType getType() { return MessageType.LEAVE_EVENT_REQUEST; }

	public int eventId;
}
