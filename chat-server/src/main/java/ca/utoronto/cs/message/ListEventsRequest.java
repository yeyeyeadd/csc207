package ca.utoronto.cs.message;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
public class ListEventsRequest extends Request{
	/**
	 * return a LIST_EVENTS_REQUEST, type of MessageType
	 * @return a LIST_EVENTS_REQUEST, type of MessageType
	 */
	@Override
	protected MessageType getType() { return MessageType.LIST_EVENTS_REQUEST; }
}
