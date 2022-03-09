package ca.utoronto.cs.requesthandler;

import ca.utoronto.cs.exception.GenericErrorException;
import ca.utoronto.cs.message.BroadcastAudiencesRequest;
import ca.utoronto.cs.message.BroadcastAudiencesResponse;
import ca.utoronto.cs.message.MessageResponse;
import ca.utoronto.cs.entity.Event;
import ca.utoronto.cs.entity.Message;
import ca.utoronto.cs.entity.User;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Date;

public class BroadcastAudiencesRequestHandler extends RequestHandler<BroadcastAudiencesRequest> {
	private final Logger logger = LogManager.getLogger();

	public BroadcastAudiencesRequestHandler(Class<BroadcastAudiencesRequest> cls) {
		super(cls);
	}

	/**
	 * To handle BroadcastAudiencesRequest.
	 * 1. check if content is valid.
	 * 2. check if user is speaker for this event.
	 * 3. Broadcast all user who in this event.
	 * @param ctx status
	 * @param req content from user.
	 */
	@Override
	public BroadcastAudiencesResponse _handle(ChannelHandlerContext ctx, BroadcastAudiencesRequest req) {
		BroadcastAudiencesResponse resp = new BroadcastAudiencesResponse();

		// Get current user
		User me = connMgr.getUserByChannel(ctx.channel());

		// Get event
		Event event = eventMgr.getEventById(req.eventId);
//		for (int speaker : event.getSpeakers()){
//			if (speaker != me.getId()) {
//				logger.info("only speaker of the event can broadcast messages to audiences");
//				throw new GenericErrorException(
//						"only speaker of the event can broadcast messages to audiences");
//			}
//		}

		MessageResponse resp2 = new MessageResponse();
		resp2.content = new Message();
		resp2.content.setContent(req.content);
		resp2.content.setFrom_alias("Speaker of the event");
		resp2.content.setFromUserId(me.getId());
		resp2.content.setTime(new Date());
		resp.audiences = new ArrayList<>();
		for(int id: event.getParticipants()) {
			resp.audiences.add(userMgr.getUserById(id));
		}
		for (User participant: resp.audiences) {
			logger.debug("sending message to {}", participant);
			resp2.content.setToUserId(participant.getId());
			for (Channel channel: connMgr.getChannelsByUser(participant)) {
				channel.writeAndFlush(resp2.putHeader(requestId));
			}
		}

		return resp;
	}
}
