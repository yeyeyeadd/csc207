package ca.utoronto.cs.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public abstract class Response {
	abstract protected MessageType getType();
	private final ObjectMapper mapper = new ObjectMapper().disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);;

	/**
	 *
	 * @param reqId request Id
	 * @return a response with header, type of ResponseHeader
	 */
	public ResponseHeader putHeader(String reqId) {
		ResponseHeader resp = new ResponseHeader();
		resp.type = getType();
		if (resp.type.getValue() >= 900)
			resp.status = false;
		else
			resp.status = true;
		resp.requestId = reqId;
		try {
			resp.content = mapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return resp;
	}

	public ResponseHeader putHeader() {
		return this.putHeader("");
	}
}
