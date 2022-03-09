package ca.utoronto.cs.requesthandler;

import ca.utoronto.cs.entity.Role;
import ca.utoronto.cs.entity.Room;
import ca.utoronto.cs.entity.User;
import ca.utoronto.cs.exception.GenericErrorException;
import ca.utoronto.cs.exception.RoomAlreadyExistedExecption;
import ca.utoronto.cs.message.*;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CreateRoomRequestHandler extends RequestHandler<CreateRoomRequest>{
    private final Logger logger = LogManager.getLogger();

    public CreateRoomRequestHandler(Class<CreateRoomRequest> cls) {
        super(cls);
    }

    @Override
    protected CreateRoomResponse _handle(ChannelHandlerContext ctx, CreateRoomRequest req) {
        CreateRoomResponse resp = new CreateRoomResponse();

        User currUser = connMgr.getUserByChannel(ctx.channel());

        if (!currUser.getRole().equals(Role.OPERATOR)){
            logger.info("User is not operator");
            throw new GenericErrorException("User is not operator");
        }
        Room room;
        String name = req.name;
        int capacity = req.capacity;
        try {
            room = roomMgr.createRoom(name, capacity);
        }
        catch (RoomAlreadyExistedExecption e){
            logger.info("Room name exist");
            throw new GenericErrorException("Room name exist");
        }
        logger.info("room created");
        resp.room = room;
        return resp;

    }
}
