package ca.utoronto.cs.message;


public class ResponseHeader {
	public boolean status;
	public MessageType type;
	public String requestId = "";
	public String content;

	/**
	 * override ToString method.
	 * @return String type of ResponseHeader.
	 */
	@Override
	public String toString() {
		return String.format("ResponseHeader: status=%s, type=%s, content=%s, reqId=%s",
				status, type, content, requestId);
	}
}
