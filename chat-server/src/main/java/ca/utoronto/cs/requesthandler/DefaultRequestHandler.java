package ca.utoronto.cs.requesthandler;

import ca.utoronto.cs.message.GenericErrorResponse;
import ca.utoronto.cs.message.UnknownRequest;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DefaultRequestHandler extends RequestHandler<UnknownRequest> {
	private final Logger logger = LogManager.getLogger();

	public DefaultRequestHandler(Class<UnknownRequest> cls) {
		super(cls);
	}

	/**
	 * To handle DefaultRequest
	 * @param ctx status
	 * @param req content from user.
	 */
	@Override
	public GenericErrorResponse _handle(ChannelHandlerContext ctx, UnknownRequest req) {
		logger.warn("This is default dump-only resquest handler");
		logger.debug("Received message: {}", req.content);
		return null;
	}
}
