package ca.utoronto.cs.requesthandler;

import ca.utoronto.cs.entity.Event;
import ca.utoronto.cs.entity.User;
import ca.utoronto.cs.exception.GenericErrorException;
import ca.utoronto.cs.exception.ObjectNotFoundException;
import ca.utoronto.cs.message.GenericErrorResponse;
import ca.utoronto.cs.message.JoinEventRequest;
import ca.utoronto.cs.message.LeaveEventRequest;
import ca.utoronto.cs.message.LeaveEventResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LeaveEventRequestHandler extends RequestHandler<LeaveEventRequest> {
    private final Logger logger = LogManager.getLogger();

    public LeaveEventRequestHandler(Class<LeaveEventRequest> cls) {
        super(cls);
    }

    /**
     * To handle LeaveEventRequestH.
     * 1.check if content is valid.
     * 2.check if event exist.
     * 4.remove user from this event, if user in this event.
     * @param ctx status
     * @param req content from user.
     */
    @Override
    public LeaveEventResponse _handle(ChannelHandlerContext ctx, LeaveEventRequest req) {
        User user = connMgr.getUserByChannel(ctx.channel());
        LeaveEventResponse resp = new LeaveEventResponse();

        Event event;
        try {
            event = eventMgr.getEventById(req.eventId);
        }
        catch (ObjectNotFoundException e){
            throw new GenericErrorException("cannot find event");
        }


        event.getParticipants().remove(new Integer(user.getId()));
        eventMgr.save(event);
        resp.event = event;
        logger.info("left event");
        return resp;
    }
}
