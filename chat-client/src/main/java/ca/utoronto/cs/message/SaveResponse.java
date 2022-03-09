package ca.utoronto.cs.message;

public class SaveResponse extends Response {
	/**
	 * return a SAVE_RESPONSE, type of MessageType
	 * @return a SAVE_RESPONSE, type of MessageType
	 */
	@Override
	protected MessageType getType() {
		return MessageType.SAVE_RESPONSE;
	}


}
