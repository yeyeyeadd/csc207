package ca.utoronto.cs;

import ca.utoronto.cs.service.*;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;


public class ChatServerInitialiser extends ChannelInitializer<SocketChannel> {
    private final int MAX_FRAME_SIZE = 8192;

    private final ConnectionManager connMgr;
    private final EventManager eventMgr;
    private final RoomManager roomMgr;
    private final UserManager userMgr ;
    private final MessageManager messageMgr;

    public ChatServerInitialiser(ConnectionManager connMgr,
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
    protected void initChannel(SocketChannel socketChannel) {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast("framer", new LineBasedFrameDecoder(MAX_FRAME_SIZE));
        pipeline.addLast("decoder-string", new StringDecoder());
        pipeline.addLast("decoder-message", new StringToMessageHeaderDecoder());
        pipeline.addLast("encoder-string", new StringEncoder());
        pipeline.addLast("encoder-response", new ResponseHeaderToStringEncoder());
        pipeline.addLast("handler", new ChatServerHandler(
                connMgr, eventMgr, roomMgr, userMgr, messageMgr
        ));
    }
}
