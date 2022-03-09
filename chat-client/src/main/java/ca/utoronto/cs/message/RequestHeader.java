package ca.utoronto.cs.message;

public class RequestHeader {
	public MessageType type;
	public String content;
	public String requestId;

	/**
	 * override toString method
	 * @return String of RequestHeader
	 */
	@Override
	public String toString() {
		return String.format("RequestHeader: type=%s, content=%s, reqId=%s",
				type, content, requestId);
	}
}
