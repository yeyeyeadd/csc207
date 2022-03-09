package ca.utoronto.cs.message;

import ca.utoronto.cs.entity.User;

import java.util.List;

public class BroadcastAudiencesResponse extends Response {
	/**
	 *
	 * @return a BROADCAST_AUDIENCES_REQUEST, type of MessageType
	 */
	@Override
	protected MessageType getType() {
		return MessageType.BROADCAST_AUDIENCES_RESPONSE;
	}

	public List<User> audiences;
}
