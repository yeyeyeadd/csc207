package ca.utoronto.cs.requesthandler;

import ca.utoronto.cs.entity.Role;
import ca.utoronto.cs.exception.GenericErrorException;
import ca.utoronto.cs.message.GenericErrorResponse;
import ca.utoronto.cs.message.ListEventsRequest;
import ca.utoronto.cs.message.ListEventsResponse;
import ca.utoronto.cs.entity.Event;
import ca.utoronto.cs.entity.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class ListEventsRequestHandler extends RequestHandler<ListEventsRequest> {
	private final Logger logger = LogManager.getLogger();

	public ListEventsRequestHandler(Class<ListEventsRequest> cls) {
		super(cls);
	}

	/**
	 * To handle ListEventsRequest.
	 * 1.check if content is valid.
	 * 2.check if user is not anonymous.
	 * 3.get all event to user.
	 * @param ctx status
	 * @param req content from user.
	 */
	@Override
	public ListEventsResponse _handle(ChannelHandlerContext ctx, ListEventsRequest req) {
		User user = connMgr.getUserByChannel(ctx.channel());
		ListEventsResponse resp = new ListEventsResponse();

		if (user.getRole().equals(Role.ANONYMOUS)) {
			throw new GenericErrorException("anonymous user is forbidden");
		}

		List<Event> events = eventMgr.getAllEvents();
		resp.events = events;
		return resp;
	}
}
