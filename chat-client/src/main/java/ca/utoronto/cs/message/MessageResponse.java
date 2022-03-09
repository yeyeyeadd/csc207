package ca.utoronto.cs.message;

import ca.utoronto.cs.entity.Message;

public class MessageResponse extends Response {
	/**
	 *return a MESSAGE_RESPONSE, type of MessageType
	 * @return a MESSAGE_RESPONSE, type of MessageType
	 */
	@Override
	protected MessageType getType() {
		return MessageType.MESSAGE_RESPONSE;
	}

	public Message content;

}
