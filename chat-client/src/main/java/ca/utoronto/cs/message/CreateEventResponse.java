package ca.utoronto.cs.message;

import ca.utoronto.cs.entity.Event;

public class CreateEventResponse extends Response {
	/**
	 * return a CREATE_EVENT_RESPONSE, type of MessageType
	 * @return a CREATE_EVENT_RESPONSE, type of MessageType
	 */
	@Override
	protected MessageType getType() {
		return MessageType.CREATE_EVENT_RESPONSE;
	}

	public int eventId;
	public Event event;
}
