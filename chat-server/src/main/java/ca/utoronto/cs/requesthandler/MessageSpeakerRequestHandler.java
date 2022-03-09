package ca.utoronto.cs.requesthandler;

import ca.utoronto.cs.exception.GenericErrorException;
import ca.utoronto.cs.message.MessageResponse;
import ca.utoronto.cs.message.MessageSpeakerRequest;
import ca.utoronto.cs.message.MessageSpeakerResponse;
import ca.utoronto.cs.entity.Event;
import ca.utoronto.cs.entity.Message;
import ca.utoronto.cs.entity.User;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MessageSpeakerRequestHandler extends RequestHandler<MessageSpeakerRequest> {
	private final Logger logger = LogManager.getLogger();

	public MessageSpeakerRequestHandler(Class<MessageSpeakerRequest> cls) {
		super(cls);
	}

	/**
	 * To handle CreateEventRequest.
	 * 1.check if content is valid.
	 * 2.check if speaker exist.
	 * 3.check if user are operate or join the speaker' event.
	 * 4.send message to this speaker
	 * @param ctx status
	 * @param req content from user.
	 */
	@Override
	public MessageSpeakerResponse _handle(ChannelHandlerContext ctx, MessageSpeakerRequest req) {
		MessageSpeakerResponse resp = new MessageSpeakerResponse();

		// Get current user
		User me = connMgr.getUserByChannel(ctx.channel());

		// Get event
		Event event = eventMgr.getEventById(req.eventId);
//		if (!event.getParticipants().contains(me)) {
//			logger.info("only participants of the event can send messages to the speaker");
//			throw new GenericErrorException(
//					"only participants of the event can send messages to the speaker");
//		}
		if (event.getSpeakers().isEmpty()){
			logger.info("there are not speaker for this event");
			throw new GenericErrorException("there are no speaker for this event");
		}
		// Get speaker
		for (int speakerid : event.getSpeakers()){
			User speaker = userMgr.getUserById(speakerid);

			Message msg = msgMgr.createMessage(event, me, speaker,
					"sent to " + speaker.getUsername() + " : " + req.content);

			MessageResponse resp2 = new MessageResponse();
			resp2.content = msg;

		// Get the speaker's channel
			for (Channel channel: connMgr.getChannelsByUser(speaker)) {
				logger.debug("sending message to speaker at channel {}", channel);
				channel.writeAndFlush(resp2.putHeader(requestId));
			}

			resp.content = resp2.content;
			ctx.channel().writeAndFlush(resp.putHeader(requestId));
		}

		// TODO: you never put resp together!!

		return resp;
	}
}
