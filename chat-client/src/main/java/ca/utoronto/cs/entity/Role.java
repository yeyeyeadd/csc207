package ca.utoronto.cs.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.stream.Stream;

public enum Role {
	ANONYMOUS(0),
	OPERATOR(1),
	USER(2),
	VIP(3);

	private final int value;
	/**
	 * set the newValue of this Role
	 *
	 * @param newValue the newvalue of Role
	 */
	Role(final int newValue) {
		value = newValue;
	}
	/**
	 * Return the value of this Role
	 *
	 * @return the value of this Role
	 */
	@JsonValue
	public int getValue() { return value; }
	/**
	 * Change the value of this Role from int to enum
	 *
	 * @param value the value of Role
	 */
	@JsonCreator
	public static Role fromValue(int value) {
		return Stream.of(Role.values()).filter(
				state -> state.value == value).findFirst().get();
	}
}
