package ca.utoronto.cs.requesthandler;

import ca.utoronto.cs.entity.Role;
import ca.utoronto.cs.entity.Room;
import ca.utoronto.cs.entity.User;
import ca.utoronto.cs.exception.GenericErrorException;
import ca.utoronto.cs.exception.ObjectNotFoundException;
import ca.utoronto.cs.message.DeleteRoomRequest;
import ca.utoronto.cs.message.DeleteRoomResponse;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class DeleteRoomRequestHandler extends RequestHandler<DeleteRoomRequest> {
    private final Logger logger = LogManager.getLogger();

    public DeleteRoomRequestHandler(Class<DeleteRoomRequest> cls) {
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
    public DeleteRoomResponse _handle(ChannelHandlerContext ctx, DeleteRoomRequest req) {
        DeleteRoomResponse resp = new DeleteRoomResponse();

        User organiser = connMgr.getUserByChannel(ctx.channel());
        if (!organiser.getRole().equals(Role.OPERATOR)) {
            logger.warn("user ({}) role is not organiser", organiser);
            throw new GenericErrorException("user role is not organiser");
        }

        Room room;
        try {
            room = roomMgr.getRoomById(req.roomId);
        }
        catch (ObjectNotFoundException e){
            throw new GenericErrorException("room not found");
        }

        roomMgr.delete(room);
        logger.info("room deleted");
        return resp;
    }
}
