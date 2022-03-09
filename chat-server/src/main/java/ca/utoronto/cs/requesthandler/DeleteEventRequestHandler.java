package ca.utoronto.cs.requesthandler;

import ca.utoronto.cs.entity.Event;
import ca.utoronto.cs.entity.Role;
import ca.utoronto.cs.entity.User;
import ca.utoronto.cs.exception.GenericErrorException;
import ca.utoronto.cs.exception.ObjectNotFoundException;
import ca.utoronto.cs.message.DeleteEventRequest;
import ca.utoronto.cs.message.DeleteEventResponse;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;

public class DeleteEventRequestHandler extends RequestHandler<DeleteEventRequest> {
    private final Logger logger = LogManager.getLogger();

    public DeleteEventRequestHandler(Class<DeleteEventRequest> cls) {
        super(cls);
    }

    /**
     * To handle DeleteEventRequest.
     * 1.check if content is valid.
     * 2.check user allow to delete.
     * 3.check event exist.
     * 4.delete event
     * @param ctx status
     * @param req content from user.
     */
    @Override
    public DeleteEventResponse _handle(ChannelHandlerContext ctx, DeleteEventRequest req) {
        DeleteEventResponse resp = new DeleteEventResponse();

        User organiser = connMgr.getUserByChannel(ctx.channel());
        if (!organiser.getRole().equals(Role.OPERATOR)) {
            logger.warn("user ({}) role is not organiser", organiser);
            throw new GenericErrorException("user role is not organiser");
        }


        Event event;
        try {
            event = eventMgr.getEventById(req.eventId);
        }
        catch (ObjectNotFoundException e){
            throw new GenericErrorException("event not found");
        }

        if(event.getStart().before(new Date())){
            throw new GenericErrorException("event already happened");
        }
        eventMgr.delete(event);
        logger.info("event deleted");
        return resp;
    }
}
