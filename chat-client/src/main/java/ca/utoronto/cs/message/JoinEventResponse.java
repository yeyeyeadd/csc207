package ca.utoronto.cs.message;

import ca.utoronto.cs.entity.Event;


public class JoinEventResponse extends Response {
	/**
	 * return a JOIN_EVENT_RESPONSE, type of MessageType
	 * @return a JOIN_EVENT_RESPONSE, type of MessageType
	 */
	@Override
	protected MessageType getType() {
		return MessageType.JOIN_EVENT_RESPONSE;
	}

	public Event event;

}
