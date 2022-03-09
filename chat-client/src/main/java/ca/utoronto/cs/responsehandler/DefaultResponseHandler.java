package ca.utoronto.cs.responsehandler;

import ca.utoronto.cs.message.UnknownResponse;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DefaultResponseHandler extends ResponseHandler<UnknownResponse> {
	private final Logger logger = LogManager.getLogger();

	public DefaultResponseHandler(Class<UnknownResponse> cls) {
		super(cls);
	}

	/**
	 *To show the response of DefaultRequest
	 * @param ctx ctx
	 * @param resp giving content
	 */
	@Override
	public void _handle(ChannelHandlerContext ctx, UnknownResponse resp) {
		logger.warn("This is default dump-only response handler");
		logger.debug("Received message: {}", resp.content);
	}
}
