package ca.utoronto.cs.message;

import ca.utoronto.cs.entity.Event;
import ca.utoronto.cs.entity.EventType;

import java.util.ArrayList;
import java.util.List;

public class CreateEventRequest extends Request {
	/**
	 * return a CREATE_EVENT_REQUEST, type of MessageType
	 *
	 * @return a CREATE_EVENT_REQUEST, type of MessageType
	 */
	@Override
	protected MessageType getType() {
		return MessageType.CREATE_EVENT_REQUEST;
	}

	public String name;
	public String room;
	public List<String> speaker;
	public String startTime;
	public String endTime;
	public EventType type;
	public int capacity;
}

