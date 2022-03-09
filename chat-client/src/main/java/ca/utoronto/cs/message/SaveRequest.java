package ca.utoronto.cs.message;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
public class SaveRequest extends Request {
	/**
	 * return a SAVE_REQUEST, type of MessageType
	 * @return a SAVE_REQUEST, type of MessageType
	 */
	@Override
	protected MessageType getType() {
		return MessageType.SAVE_REQUEST;
	}
}
