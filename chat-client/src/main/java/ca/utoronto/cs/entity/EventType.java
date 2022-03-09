package ca.utoronto.cs.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.stream.Stream;

public enum EventType {
	SOLO(0),
	MULTIPLE(1),
	NONE(2),
	VIPEVENT(3);

	private final int value;


	EventType(final int newValue) {
		value = newValue;
	}

	@JsonValue
	public int getValue() {
		return value;
	}

	@JsonCreator
	public static EventType fromValue(int value) {
		return Stream.of(EventType.values()).filter(
				state -> state.value == value).findFirst().get();
	}
}
