package ca.utoronto.cs.responsehandler;

import ca.utoronto.cs.RequestManager;
import ca.utoronto.cs.message.GenericErrorResponse;
import ca.utoronto.cs.message.Response;
import ca.utoronto.cs.message.ResponseHeader;
import ca.utoronto.cs.message.UnknownResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class ResponseHandler<T extends Response> {
	private final Logger logger = LogManager.getLogger();
	private final Class<T> cls;
	protected final ObjectMapper mapper = new ObjectMapper();
	protected RequestManager reqMgr;
	protected String requestId = null;

	public ResponseHandler(Class<T> cls) {
		this.cls = cls;
	}

	protected abstract void _handle(ChannelHandlerContext ctx, T resp);

	public void setRequestId(String reqId) {
		this.requestId = reqId;
	}

	protected T decodeMessage(ChannelHandlerContext ctx, String content) {
		T req;
		try {
			req = mapper.readValue(content, cls);
		} catch (JsonProcessingException e) {
			return null;
		}
		return req;
	}

	public void handle(ChannelHandlerContext ctx, ResponseHeader header) {
		String content = header.content;
		T msg = null;
		if (!cls.equals(UnknownResponse.class))
			msg = decodeMessage(ctx, content);
		else {
			UnknownResponse uresp = new UnknownResponse();
			uresp.content = content;
			msg = (T)uresp;
		}
		if (msg == null) {
			logger.warn("unable to decode message, not processing");
			return;
		}
		//this._handle(ctx, msg);		// TODO: remove all handlers in next version. no longer useful
		reqMgr.handle(msg, header);
	}

	public void setRequestManager(RequestManager reqMgr) {
		this.reqMgr = reqMgr;
	}

}
