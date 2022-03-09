package ca.utoronto.cs.requesthandler;

import ca.utoronto.cs.entity.Message;
import ca.utoronto.cs.entity.Role;
import ca.utoronto.cs.entity.User;
import ca.utoronto.cs.exception.GenericErrorException;
import ca.utoronto.cs.exception.ObjectNotFoundException;
import ca.utoronto.cs.message.GenericErrorResponse;
import ca.utoronto.cs.message.MessageFriendRequest;
import ca.utoronto.cs.message.MessageFriendResponse;
import ca.utoronto.cs.message.MessageResponse;
import ca.utoronto.cs.service.ConnectionManager;
import ca.utoronto.cs.service.MessageManager;
import ca.utoronto.cs.service.UserManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MessageFriendRequestHandler extends RequestHandler<MessageFriendRequest> {
    private final Logger logger = LogManager.getLogger();

    public MessageFriendRequestHandler(Class<MessageFriendRequest> cls) {
        super(cls);
    }

    /**
     * To handle MessageFriendRequest.
     * 1.check if content is valid.
     * 2.check both user are exist and friend.
     * 3.send message.
     * @param ctx status
     * @param req content from user.
     */
    @Override
    public MessageFriendResponse _handle(ChannelHandlerContext ctx, MessageFriendRequest req) {
        MessageFriendResponse resp = new MessageFriendResponse();

        // Get receiver
        User receiver = null;
        try {
            receiver = userMgr.getUserByName(req.receiver);
        }
        catch (ObjectNotFoundException e) {
            throw new GenericErrorException("Receiver not found");
        }

        // Get sender
        User sender = connMgr.getUserByChannel(ctx.channel());

        //check sender
        if (sender.getRole().equals(Role.ANONYMOUS)) {
            throw new GenericErrorException("anonymous user is forbidden");
        }


//        if(sender.getRole().equals(Role.USER) && !sender.getFriends().contains(receiver)){
//            throw new GenericErrorException("receiver not in friend list");
//        }

        Message msgObj = msgMgr.createMessage(sender, receiver,
                "send to " + receiver.getUsername() + " : " + req.msg);

        MessageResponse msgResp = new MessageResponse();
        //let all users receive the message
        for(Channel ch: connMgr.getChannelsByUser(userMgr.getUserByName(req.receiver))) {
            logger.debug("sending message to user at channel {}", ch);
            msgResp.content = msgObj;
            ch.writeAndFlush(msgResp.putHeader(requestId));
        }

        resp.msgObj = msgObj;
        return resp;
    }
}
