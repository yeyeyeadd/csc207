package ca.utoronto.cs.requesthandler;

import ca.utoronto.cs.entity.Role;
import ca.utoronto.cs.entity.Room;
import ca.utoronto.cs.entity.User;
import ca.utoronto.cs.exception.GenericErrorException;
import ca.utoronto.cs.message.ListRoomsRequest;
import ca.utoronto.cs.message.ListRoomsResponse;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class ListRoomsRequestHandler extends RequestHandler<ListRoomsRequest> {
    private final Logger logger = LogManager.getLogger();

    public ListRoomsRequestHandler(Class<ListRoomsRequest> cls) {
        super(cls);
    }

    /**
     * To handle ListEventsRequest.
     * 1.check if content is valid.
     * 2.check if user is not anonymous.
     * 3.get all event to user.
     * @param ctx status
     * @param req content from user.
     */
    @Override
    public ListRoomsResponse _handle(ChannelHandlerContext ctx, ListRoomsRequest req) {
        User user = connMgr.getUserByChannel(ctx.channel());
        ListRoomsResponse resp = new ListRoomsResponse();

        if (user.getRole().equals(Role.ANONYMOUS)) {
            throw new GenericErrorException("anonymous user is forbidden");
        }

        List<Room> rooms = roomMgr.getAllRooms();
        resp.rooms = rooms;
        return resp;
    }
}
