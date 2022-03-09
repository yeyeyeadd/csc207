package ca.utoronto.cs.responsehandler;

import ca.utoronto.cs.message.LoginResponse;
import ca.utoronto.cs.message.MessageFriendResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MessageFriendResponseHandler extends ResponseHandler<MessageFriendResponse> {
    private final Logger logger = LogManager.getLogger();

    public MessageFriendResponseHandler(Class<MessageFriendResponse> cls) {
        super(cls);
    }

    /**
     *To show the response of MessageFriendResquest
     * @param ctx ctx
     * @param resp giving content
     */
    @Override
    public void _handle(ChannelHandlerContext ctx, MessageFriendResponse resp) {
        System.out.println(String.format("Received send message to friend response: %s", resp));
    }
}
