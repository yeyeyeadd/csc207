package ca.utoronto.cs;

import ca.utoronto.cs.service.*;
import ca.utoronto.cs.message.BannerResponse;
import ca.utoronto.cs.message.GenericErrorResponse;
import ca.utoronto.cs.message.RequestHeader;
import ca.utoronto.cs.requesthandler.RequestHandler;
import ca.utoronto.cs.requesthandler.RequestHandlerFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.DecoderException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class ChatServerHandler extends SimpleChannelInboundHandler<RequestHeader> {
	private static Logger logger;
	private final ConnectionManager connMgr;
	private final EventManager eventMgr;
	private final RoomManager roomMgr;
	private final UserManager userMgr ;
	private final MessageManager messageMgr;

	public ChatServerHandler(ConnectionManager connMgr,
	                             EventManager eventMgr,
	                             RoomManager roomMgr,
	                             UserManager userMgr,
	                             MessageManager messageMgr) {
		this.connMgr = connMgr;
		this.eventMgr = eventMgr;
		this.roomMgr = roomMgr;
		this.userMgr = userMgr;
		this.messageMgr = messageMgr;
	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) {
		Channel incoming = ctx.channel();
		logger = LogManager.getLogger(ChatServerHandler.class.getName() + "."
				+ incoming.remoteAddress().toString().replace('.', '-'));
		logger.info("Incoming connection");

		BannerResponse banner = new BannerResponse();
		banner.message = "Welcome to our chat server";
		incoming.writeAndFlush(banner.putHeader());

		connMgr.addConnection(userMgr.getSystemUser(SystemUser.ANONYMOUS), incoming);
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) {
		Channel incoming = ctx.channel();
		logger.info("Disconnected");
		connMgr.removeConnection(incoming);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable throwable) {
		logger.error("{} exceptionCaught: {}",
				ctx.name(),
				throwable.toString());
		logger.error(throwable);
		GenericErrorResponse resp = new GenericErrorResponse();
		resp.message = throwable.toString();
		ctx.channel().writeAndFlush(resp.putHeader());
		if (!(throwable instanceof DecoderException)) {
			ctx.close();
		}
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, RequestHeader msg) {
		Channel incoming = ctx.channel();
		logger.debug("MessageHeader received {}: {}", incoming.remoteAddress(), msg.toString());
		RequestHandler handler = RequestHandlerFactory.fromMessageType(msg.type);
		handler.setConnectionManager(connMgr);
		handler.setEventManager(eventMgr);
		handler.setRoomManager(roomMgr);
		handler.setUserManager(userMgr);
		handler.setMessageManager(messageMgr);
		handler.setRequestId(msg.requestId);
		handler.handle(ctx, msg.content);
	}
}
