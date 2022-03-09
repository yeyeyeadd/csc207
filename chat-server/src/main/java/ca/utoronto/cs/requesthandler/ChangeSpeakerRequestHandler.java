package ca.utoronto.cs.requesthandler;

import ca.utoronto.cs.entity.Event;
import ca.utoronto.cs.entity.EventType;
import ca.utoronto.cs.entity.Role;
import ca.utoronto.cs.entity.User;
import ca.utoronto.cs.exception.EventConflictException;
import ca.utoronto.cs.exception.GenericErrorException;
import ca.utoronto.cs.exception.ObjectNotFoundException;
import ca.utoronto.cs.message.ChangeSpeakerRequest;
import ca.utoronto.cs.message.ChangeSpeakerResponse;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class ChangeSpeakerRequestHandler extends RequestHandler<ChangeSpeakerRequest> {
    private final Logger logger = LogManager.getLogger();

    public ChangeSpeakerRequestHandler(Class<ChangeSpeakerRequest> cls) {
        super(cls);
    }

    /**
     * To handle ChangeSpeakerRequest.
     * 1.check if content is valid.
     * 2.check if user is operator
     * 3.check if speaker id and event id is valid.
     * 4.change speaker for this event.
     * @param ctx status
     * @param req content from user
     */
    @Override
    public ChangeSpeakerResponse _handle(ChannelHandlerContext ctx, ChangeSpeakerRequest req) {
        ChangeSpeakerResponse resp = new ChangeSpeakerResponse();

        User organiser = connMgr.getUserByChannel(ctx.channel());
        if (!organiser.getRole().equals(Role.OPERATOR)) {
            logger.warn("user ({}) role is not organiser", organiser);
            throw new GenericErrorException("user role is not organiser");
        }

        Event event;
        try{
            event = eventMgr.getEventById(req.eventId);
        }
        catch (ObjectNotFoundException e) {
            throw new GenericErrorException("event not found");
        }
        if (event.getType().equals(EventType.NONE)){
            logger.warn("can't add a speaker to a part");
            throw new GenericErrorException("the event is a part, not need speaker");
        }

        List<User> listofspeakers = new ArrayList<>();
        for (String speaker : req.speakerName) {
            User speakers;
            try {
                speakers = userMgr.getUserByName(speaker);
            }
            catch (ObjectNotFoundException e) {
                throw new GenericErrorException("speaker not found");
            }
            listofspeakers.add(speakers);
        }
        //see if the speaker is the speaker of the event
        try {
            eventMgr.changeSpeaker(event, listofspeakers);
        }
        catch (EventConflictException e){
            throw new GenericErrorException("some speakers speaking at this time slot");
        }

        logger.info("Event speakers changed. event={}", event);
        resp.event = event;
        return resp;
    }
}
