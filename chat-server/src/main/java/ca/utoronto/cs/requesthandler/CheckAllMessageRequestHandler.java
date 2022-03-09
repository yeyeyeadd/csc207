package ca.utoronto.cs.requesthandler;

import ca.utoronto.cs.entity.Message;
import ca.utoronto.cs.entity.Role;
import ca.utoronto.cs.entity.User;
import ca.utoronto.cs.exception.GenericErrorException;
import ca.utoronto.cs.message.CheckAllMessageRequest;
import ca.utoronto.cs.message.CheckAllMessageResponse;
import ca.utoronto.cs.message.GenericErrorResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CheckAllMessageRequestHandler extends RequestHandler<CheckAllMessageRequest> {
    private final Logger logger = LogManager.getLogger();

    public CheckAllMessageRequestHandler(Class<CheckAllMessageRequest> cls) {
        super(cls);
    }

    /**
     * To handle CheckAllMessageRequest.
     * 1.check if content is valid.
     * 2.check if user is allow.
     * 3.give all message to user.
     * @param ctx status
     * @param req content from user.
     */
    @Override
    public CheckAllMessageResponse _handle(ChannelHandlerContext ctx, CheckAllMessageRequest req) {
        CheckAllMessageResponse resp = new CheckAllMessageResponse();

        User currUser = connMgr.getUserByChannel(ctx.channel());
        if(currUser.getRole().equals(Role.ANONYMOUS)){
            throw new GenericErrorException("anonymous is forbidden");
        }

        resp.messages = msgMgr.getMessagesByUserId(currUser.getId());

        logger.info("Check all messages. messages={}", resp.messages);
        return resp;
    }
}
