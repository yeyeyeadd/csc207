package ca.utoronto.cs.requesthandler;

import ca.utoronto.cs.entity.*;
import ca.utoronto.cs.exception.EventConflictException;
import ca.utoronto.cs.exception.GenericErrorException;
import ca.utoronto.cs.exception.InternalExceptionBase;
import ca.utoronto.cs.exception.ObjectNotFoundException;
import ca.utoronto.cs.message.CreateEventRequest;
import ca.utoronto.cs.message.CreateEventResponse;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CreateEventRequestHandler extends RequestHandler<CreateEventRequest> {
	private final Logger logger = LogManager.getLogger();

	public CreateEventRequestHandler(Class<CreateEventRequest> cls) {
		super(cls);
	}

	/**
	 * To handle CreateEventRequest.
	 * 1.check if content is valid.
	 * 2.check start time and end time is valid.
	 * 3.check event conflict
	 * 4.create event
	 * @param ctx status
	 * @param req content from user.
	 */
	@Override
	public CreateEventResponse _handle(ChannelHandlerContext ctx, CreateEventRequest req) {
		CreateEventResponse resp = new CreateEventResponse();

		// Get organiser
		User organiser = connMgr.getUserByChannel(ctx.channel());
		if (!organiser.getRole().equals(Role.OPERATOR)) {
			logger.warn("user ({}) role is not organiser", organiser);
			throw new GenericErrorException("user role is not organiser");
		}

		// Get room
		Room room;
		try {
			room = roomMgr.getRoom(req.room);
		}
		catch (ObjectNotFoundException e){
			logger.error("room with name {} does not exist", req.room);
			throw new GenericErrorException(String.format("room with name %s does not exist", req.room));
		}
		//get type
		EventType role = req.type;

		// Get speaker
		List<User> speakers = new ArrayList<>();
		if(role.getValue() != 2) {
			for (String u : req.speaker) {
				try {
					speakers.add(userMgr.getUserByName(u));
				} catch (InternalExceptionBase e) {
					logger.error("unable to add user name={} as speaker", u);
					logger.error(e);
					throw new GenericErrorException(String.format("unable to add user name=%s as speaker", u));
				}
			}
		}

		// Get start/end date
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		Date start = null;
		Date end = null;
		try {
			start = dateFormat.parse(req.startTime);
		} catch (ParseException e) {
			logger.error("parse start time failed");
			logger.error(e);
		}
		try {
			end = dateFormat.parse(req.endTime);
		} catch (ParseException e) {
			logger.error("parse end time failed");
			logger.error(e);
		}
		if (start == null || end == null) {
			logger.error("start or end date parse failed");
			throw new GenericErrorException("start/end time parse failed");
		}

		int capacity = req.capacity;
		if (capacity > room.getCapacity()){
			logger.warn("the max capacity for this room is ({})", room.getCapacity());
			throw new GenericErrorException(String.format(
					"the max capacity for this room is %s",
					room.getCapacity()));
		}

		Event event = null;
		try {
			event = eventMgr.createEvent(req.name, organiser, room, speakers, start, end, role, capacity);
		}
		catch (EventConflictException e) {
			throw new GenericErrorException("event conflict");
		}

		logger.info("Event created. event={}", event);
		resp.event = event;
		return resp;
	}
}
