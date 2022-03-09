package ca.utoronto.cs.requesthandler;

import ca.utoronto.cs.entity.EventType;
import ca.utoronto.cs.entity.Role;
import ca.utoronto.cs.exception.GenericErrorException;
import ca.utoronto.cs.exception.ObjectNotFoundException;
import ca.utoronto.cs.message.JoinEventRequest;
import ca.utoronto.cs.message.JoinEventResponse;
import ca.utoronto.cs.entity.Event;
import ca.utoronto.cs.entity.User;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class JoinEventRequestHandler extends RequestHandler<JoinEventRequest> {
	private final Logger logger = LogManager.getLogger();

	public JoinEventRequestHandler(Class<JoinEventRequest> cls) {
		super(cls);
	}

	/**
	 * To handle JoinEventRequest.
	 * 1.check if content is valid.
	 * 2.check if user isn't anonymous.
	 * 3.check if the event exist.
	 * 4.check if user in this event.
	 * 5.add user to this event.
	 * @param ctx status
	 * @param req content from user.
	 */
	@Override
	public JoinEventResponse _handle(ChannelHandlerContext ctx, JoinEventRequest req) {
		User user = connMgr.getUserByChannel(ctx.channel());
		JoinEventResponse resp = new JoinEventResponse();

		if (user.getRole().equals(Role.ANONYMOUS)) {
			throw new GenericErrorException("anonymous user is forbidden");
		}

		Event event;
		try {
			event = eventMgr.getEventById(req.eventId);
		}
		catch(ObjectNotFoundException e) {
			throw new GenericErrorException("event does not exist");
		}
		for (int speaker : event.getSpeakers()){
			if (speaker == user.getId()) {
				resp.event = event;
				throw new GenericErrorException("you are already the speaker of this event");
			}
		}
		if (event.getParticipants().contains(user.getId())) {
			resp.event = event;
			throw new GenericErrorException("you are already a participant of this event");
		}
		if (roomMgr.getRoomById(event.getRoomId()).getCapacity() < event.getParticipants().size()) {
			resp.event = event;
			throw new GenericErrorException("room reached max capacity");
		}
		if (event.getCapacity() <= event.getParticipants().size()){
			resp.event = event;
			throw new GenericErrorException("event reached max capacity");
		}

		if (event.getType().equals(EventType.VIPEVENT) && !user.getRole().equals(Role.VIP)) {
			resp.event = event;
			throw new GenericErrorException("only vip can join vip event");
		}
		event.getParticipants().add(user.getId());
		eventMgr.save(event);

		resp.event = event;
		return resp;
	}
}
