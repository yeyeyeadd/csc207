package ca.utoronto.cs.requesthandler;

import ca.utoronto.cs.entity.Event;
import ca.utoronto.cs.entity.Room;
import ca.utoronto.cs.exception.GenericErrorException;
import ca.utoronto.cs.exception.ObjectNotFoundException;
import ca.utoronto.cs.message.ChangeEventCapacityRequest;
import ca.utoronto.cs.message.ChangeEventCapacityResponse;
import ca.utoronto.cs.message.Response;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ChangeEventCapacityRequestHandler extends RequestHandler<ChangeEventCapacityRequest>{
    private final Logger logger = LogManager.getLogger();
    public ChangeEventCapacityRequestHandler(Class<ChangeEventCapacityRequest> cls) {
        super(cls);
    }

    @Override
    protected ChangeEventCapacityResponse _handle(ChannelHandlerContext ctx, ChangeEventCapacityRequest req) {
        ChangeEventCapacityResponse resp = new ChangeEventCapacityResponse();
        Event event;
        try {
            event = eventMgr.getEventById(req.eventid);
        }
        catch (ObjectNotFoundException e){
            throw new ObjectNotFoundException(String.valueOf(req.eventid));
        }
        Room room = roomMgr.getRoomById(event.getRoomId());
        if (room.getCapacity() < req.capacity){
            logger.error("reach the max capacity of the room");
            throw new GenericErrorException(String.format(
                    "the max capacity for this room is %s",
                    room.getCapacity()));
        }
        if(event.getParticipants().size() > req.capacity){
            logger.error("the event will have more participants than capacity");
            throw new GenericErrorException("the event will have more participants than capacity");
        }
        event.setCapacity(req.capacity);
        logger.info("the new capacity={}", req.capacity);
        resp.event = event;
        eventMgr.save(event);

        return resp;
    }
}