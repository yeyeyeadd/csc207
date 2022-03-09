package ca.utoronto.cs.message;

import ca.utoronto.cs.entity.Message;

public class MessageSpeakerResponse extends Response {
	/**
	 * return a MESSAGE_SPEAKER_RESPONSE, type of MessageType
	 * @return a MESSAGE_SPEAKER_RESPONSE, type of MessageType
	 */
	@Override
	protected MessageType getType() {
		return MessageType.MESSAGE_SPEAKER_RESPONSE;
	}

	public Message content;
}
