package ca.utoronto.cs.message;

public class MessageSpeakerRequest extends Request {
	/**
	 *return a MESSAGE_SPEAKER_REQUEST, type of MessageType
	 * @return a MESSAGE_SPEAKER_REQUEST, type of MessageType
	 */
	@Override
	protected MessageType getType() {
		return MessageType.MESSAGE_SPEAKER_REQUEST;
	}

	public int eventId;
	public String content;
}
