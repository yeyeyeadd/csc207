package ca.utoronto.cs.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class Request {
	abstract protected MessageType getType();
	private final ObjectMapper mapper = new ObjectMapper();

	/**
	 * To create a request with a header
	 * @return a request with header on,  type of RequestHeader
	 */
	public RequestHeader putHeader(String reqId) {
		RequestHeader req = new RequestHeader();
		req.type = this.getType();
		req.requestId = reqId;
		try {
			req.content = mapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return req;
	}

	public RequestHeader putHeader() {
		return this.putHeader("");
	}
}
