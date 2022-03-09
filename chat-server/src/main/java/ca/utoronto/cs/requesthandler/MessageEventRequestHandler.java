package ca.utoronto.cs.requesthandler;

import ca.utoronto.cs.entity.Event;
import ca.utoronto.cs.entity.Message;
import ca.utoronto.cs.entity.Role;
import ca.utoronto.cs.entity.User;
import ca.utoronto.cs.exception.GenericErrorException;
import ca.utoronto.cs.exception.ObjectNotFoundException;
import ca.utoronto.cs.message.*;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class MessageEventRequestHandler extends RequestHandler<MessageEventRequest> {
    private final Logger logger = LogManager.getLogger();

    public MessageEventRequestHandler(Class<MessageEventRequest> cls) {
        super(cls);
    }

    /**
     * To handle MessageEventRequest.
     * 1.check if content is valid.
     * 2.check if user is not anonymous and is a speaker for this event.
     * 3.send message to all user who in this event.
     * @param ctx status
     * @param req content from user.
     */
    @Override
    public MessageEventResponse _handle(ChannelHandlerContext ctx, MessageEventRequest req) {
        MessageEventResponse resp = new MessageEventResponse();

        User currUser = connMgr.getUserByChannel(ctx.channel());
        if(currUser.getRole().equals(Role.ANONYMOUS)){
            throw new GenericErrorException("anonymous is forbidden");
        }

        Event event;
        try{
            event = eventMgr.getEventById(req.eventId);
        }
        catch (ObjectNotFoundException e){
            throw new GenericErrorException("event not found");
        }
        for (int speaker : event.getSpeakers()){
            if(currUser.getRole().equals(Role.USER) && speaker != currUser.getId()){
                throw new GenericErrorException("event not found");
                }
            MessageResponse msgResp = new MessageResponse();
            User speakerUser = userMgr.getUserById(speaker);
            msgResp.content = msgMgr.createMessage(currUser, speakerUser,
                    "send to all in event " + event.getId() + " : " + req.message);
            for (Channel channel: connMgr.getChannelsByUser(speakerUser)) {
                logger.debug("sending message to speaker at channel {}", channel);
                channel.writeAndFlush(msgResp.putHeader(requestId));
            }
        }

        List<Message> msgLst = new ArrayList<>();

        for(int id: event.getParticipants()){
            MessageResponse msgResp = new MessageResponse();
            User receiver = userMgr.getUserById(id);
            msgResp.content = msgMgr.createMessage(currUser, receiver,
                    "send to all in event " + event.getId() + " : " + req.message);
            msgLst.add(msgResp.content);
            for (Channel channel: connMgr.getChannelsByUser(receiver)) {
                logger.debug("sending message to user at channel {}", channel);
                channel.writeAndFlush(msgResp.putHeader(requestId));
            }
        }

        resp.msgLst = msgLst;

        logger.info("message sent to all attendees of event");
        return resp;
    }
}
