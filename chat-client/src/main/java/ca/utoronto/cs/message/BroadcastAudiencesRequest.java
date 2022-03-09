package ca.utoronto.cs.message;

public class BroadcastAudiencesRequest extends Request {
	/**
	 * return a BROADCAST_AUDIENCES_REQUEST, type of MessageType
	 * @return a BROADCAST_AUDIENCES_REQUEST, type of MessageType
	 */
	@Override
	protected MessageType getType() {
		return MessageType.BROADCAST_AUDIENCES_REQUEST;
	}

	public int eventId;
	public String content;
}
