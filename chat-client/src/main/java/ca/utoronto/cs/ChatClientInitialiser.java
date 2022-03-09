package ca.utoronto.cs;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class ChatClientInitialiser extends ChannelInitializer<SocketChannel> {
	private final int MAX_FRAME_SIZE = 8192;
	private final RequestManager reqMgr;

	public ChatClientInitialiser(RequestManager reqMgr) {
		this.reqMgr = reqMgr;
	}

	@Override
	protected void initChannel(SocketChannel socketChannel) {
		ChannelPipeline pipeline = socketChannel.pipeline();
		pipeline.addLast("framer", new LineBasedFrameDecoder(MAX_FRAME_SIZE));
		pipeline.addLast("decoder-string", new StringDecoder());
		pipeline.addLast("decoder-message", new StringToResponseHeaderDecoder());
		pipeline.addLast("encoder-string", new StringEncoder());
		pipeline.addLast("encoder-request", new RequestHeaderToStringEncoder());
		pipeline.addLast("handler", new ChatClientHandler(reqMgr));
	}
}
