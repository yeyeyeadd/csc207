package ca.utoronto.cs.exception;

import ca.utoronto.cs.message.Response;

public class GenericResponseException extends ResponseExceptionBase {
	private final Response response;
	public GenericResponseException(Response resp) {
		this.response = resp;
	}

	public Response getResponse() {
		return this.response;
	}
}
