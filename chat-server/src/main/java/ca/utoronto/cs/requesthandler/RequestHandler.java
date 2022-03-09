package ca.utoronto.cs.requesthandler;

import ca.utoronto.cs.exception.InternalExceptionBase;
import ca.utoronto.cs.exception.ResponseExceptionBase;
import ca.utoronto.cs.message.GenericErrorResponse;
import ca.utoronto.cs.message.Request;
import ca.utoronto.cs.message.Response;
import ca.utoronto.cs.message.UnknownRequest;
import ca.utoronto.cs.service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * create manager for all handler.
 */
public abstract class RequestHandler<T extends Request> {
	private final Logger logger = LogManager.getLogger();
	protected final ObjectMapper mapper = new ObjectMapper();
	protected ConnectionManager connMgr;
	protected UserManager userMgr;
	protected EventManager eventMgr;
	protected RoomManager roomMgr;
	protected MessageManager msgMgr;
	protected String requestId;
	private final Class<T> cls;

	public RequestHandler(Class<T> cls) {
		this.cls = cls;
	}

	protected abstract Response _handle(ChannelHandlerContext ctx, T msg);

	public void setConnectionManager(ConnectionManager connMgr) {
		this.connMgr = connMgr;
	}

	public void setEventManager(EventManager eventMgr) {
		this.eventMgr = eventMgr;
	}

	public void setRoomManager(RoomManager roomMgr) {
		this.roomMgr = roomMgr;
	}

	public void setUserManager(UserManager userMgr) {
		this.userMgr = userMgr;
	}

	public void setMessageManager(MessageManager msgMgr) {
		this.msgMgr = msgMgr;
	}

	public void setRequestId(String reqId) { this.requestId = reqId; }

	protected T decodeMessage(ChannelHandlerContext ctx, String content) {
		T req;
		try {
			req = mapper.readValue(content, cls);
		} catch (JsonProcessingException e) {
			return null;
		}
		return req;
	}

	public void handle(ChannelHandlerContext ctx, String content) {
		T msg = null;
		if (!cls.equals(UnknownRequest.class))
			msg = decodeMessage(ctx, content);
		else {
			UnknownRequest ureq = new UnknownRequest();
			ureq.content = content;
			msg = (T)ureq;
		}
		if (msg == null) {
			logger.warn("unable to decode message, not processing");
			ctx.channel().writeAndFlush(new GenericErrorResponse("unable to decode message content")
					.putHeader(requestId));
			return;
		}

		Response resp = null;
		try {
			resp = this._handle(ctx, msg);
		}
		catch (ResponseExceptionBase e) {
			ctx.channel().writeAndFlush(new GenericErrorResponse(e.getMessage()).putHeader(requestId));
		}

		if (resp != null)
			ctx.channel().writeAndFlush(resp.putHeader(requestId));
	}
}
