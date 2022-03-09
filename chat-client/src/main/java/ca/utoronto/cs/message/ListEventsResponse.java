package ca.utoronto.cs.message;

import ca.utoronto.cs.entity.Event;

import java.util.List;

public class ListEventsResponse extends Response {
	/**
	 * return a LIST_EVENTS_RESPONSE, type of MessageType
	 * @return a LIST_EVENTS_RESPONSE, type of MessageType
	 */
	@Override
	protected MessageType getType() { return MessageType.LIST_EVENTS_RESPONSE; }

	public List<Event> events;
}
