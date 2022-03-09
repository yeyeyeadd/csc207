package ca.utoronto.cs.requesthandler;

import ca.utoronto.cs.entity.Event;
import ca.utoronto.cs.entity.Message;
import ca.utoronto.cs.entity.Role;
import ca.utoronto.cs.entity.User;
import ca.utoronto.cs.exception.GenericErrorException;
import ca.utoronto.cs.message.*;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class MessageAllSpeakerRequestHandler extends RequestHandler<MessageAllSpeakerRequest> {
    private final Logger logger = LogManager.getLogger();

    public MessageAllSpeakerRequestHandler(Class<MessageAllSpeakerRequest> cls) {
        super(cls);
    }

    /**
     * To handle MessageAllSpeakerRequest.
     * 1.check if content is valid.
     * 2.check if user is operator.
     * 3.send message to all speaker.
     * @param ctx status
     * @param req content from user.
     */
    @Override
    public MessageAllSpeakerResponse _handle(ChannelHandlerContext ctx, MessageAllSpeakerRequest req) {
        MessageAllSpeakerResponse resp = new MessageAllSpeakerResponse();

        User currUser = connMgr.getUserByChannel(ctx.channel());

        if(!currUser.getRole().equals(Role.OPERATOR)){
            throw new GenericErrorException("user not operator");
        }

        List<Event> events = eventMgr.getAllEvents();
        List<Message> msgs = new ArrayList<>();
        //for all events
        for(Event event: events){
            for(int u : event.getSpeakers()){
                User speaker = userMgr.getUserById(u);
                MessageResponse msgResp = new MessageResponse();
                msgResp.content = msgMgr.createMessage(currUser, speaker, "send to all speaker: " + req.message);
                msgs.add(msgResp.content);
                //for all channels of the speaker
                for (Channel channel: connMgr.getChannelsByUser(speaker)) {
                    logger.debug("sending message to speaker at channel {}", channel);
                    channel.writeAndFlush(msgResp.putHeader(requestId));
                }
            }
        }

        resp.msgLst = msgs;

        logger.info("message sent to all speakers");
        return resp;
    }
}
