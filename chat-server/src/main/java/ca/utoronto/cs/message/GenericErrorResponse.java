package ca.utoronto.cs.message;

public class GenericErrorResponse extends Response {
	/**
	 * return a ERROR, type of MessageType
	 * @return a ERROR, type of MessageType
	 */
	@Override
	protected MessageType getType() {
		return MessageType.ERROR;
	}

	public GenericErrorResponse(String msg) {
		this.message = msg;
	}

	public GenericErrorResponse() {
		this.message = "";
	}

	public String message;
}
