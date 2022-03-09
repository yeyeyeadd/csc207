package ca.utoronto.cs.requesthandler;

import ca.utoronto.cs.entity.Role;
import ca.utoronto.cs.exception.GenericErrorException;
import ca.utoronto.cs.message.GenericErrorResponse;
import ca.utoronto.cs.message.SaveRequest;
import ca.utoronto.cs.message.SaveResponse;
import ca.utoronto.cs.entity.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SaveRequestHandler extends RequestHandler<SaveRequest> {
	private final Logger logger = LogManager.getLogger();

	public SaveRequestHandler(Class<SaveRequest> cls) {
		super(cls);
	}

	/**
	 * To handle SaveRequest.
	 * 1.check if content is valid.
	 * 2.check if user is operator
	 * 3.save the info.
	 * @param ctx status
	 * @param req content from user.
	 */
	@Override
	public SaveResponse _handle(ChannelHandlerContext ctx, SaveRequest req) {
		User user = connMgr.getUserByChannel(ctx.channel());
		SaveResponse resp = new SaveResponse();

		if (!user.getRole().equals(Role.OPERATOR)) {
			throw new GenericErrorException("no permission");
		}

		logger.info("saving world");
//		this.connMgr.flush("conn.json");
//		this.eventMgr.flush("event.json");
//		this.userMgr.flush("user.json");
//		this.roomMgr.flush("room.json");
		logger.info("saved");

		return resp;
	}
}
