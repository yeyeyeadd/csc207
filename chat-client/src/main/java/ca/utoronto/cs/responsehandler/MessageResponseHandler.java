package ca.utoronto.cs.responsehandler;

import ca.utoronto.cs.entity.Message;
import ca.utoronto.cs.message.MessageFriendResponse;
import ca.utoronto.cs.message.MessageResponse;
import ca.utoronto.cs.message.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MessageResponseHandler extends ResponseHandler<MessageResponse> {
    private final Logger logger = LogManager.getLogger();

    public MessageResponseHandler(Class<MessageResponse> cls) {
        super(cls);
    }

    /**
     *To show the response of MessageRequest
     * @param ctx ctx
     * @param resp giving content
     */
    @Override
    public void _handle(ChannelHandlerContext ctx, MessageResponse resp) {
        System.out.println(String.format("Received a message: %s", resp.content));
    }
}
