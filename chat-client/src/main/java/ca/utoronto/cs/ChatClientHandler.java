package ca.utoronto.cs;

import ca.utoronto.cs.message.ResponseHeader;
import ca.utoronto.cs.responsehandler.ResponseHandler;
import ca.utoronto.cs.responsehandler.ResponseHandlerFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ChatClientHandler extends SimpleChannelInboundHandler<ResponseHeader> {
	private static final Logger logger = LogManager.getLogger();
	private final RequestManager reqMgr;

	public ChatClientHandler(RequestManager reqMgr) {
		super();
		this.reqMgr = reqMgr;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ResponseHeader msg) {
		Channel incoming = ctx.channel();
		logger.debug("MessageHeader received {}: {}", incoming.remoteAddress(), msg.toString());
		ResponseHandler handler = ResponseHandlerFactory.fromMessageType(msg.type);
		handler.setRequestManager(reqMgr);
		handler.setRequestId(msg.requestId);
		handler.handle(ctx, msg);
	}
}
