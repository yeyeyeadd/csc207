package ca.utoronto.cs.responsehandler;

import ca.utoronto.cs.entity.Room;
import ca.utoronto.cs.message.ListRoomsResponse;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ListRoomsResponseHandler extends ResponseHandler<ListRoomsResponse> {
    private final Logger logger = LogManager.getLogger();

    public ListRoomsResponseHandler(Class<ListRoomsResponse> cls) {
        super(cls);
    }

    /**
     *To show the response of ListEventsRequest
     * @param ctx ctx
     * @param resp giving content
     */
    @Override
    public void _handle(ChannelHandlerContext ctx, ListRoomsResponse resp) {
        System.out.print("All rooms: ");
        if (resp.rooms != null)
            for(Room r: resp.rooms){
                System.out.println(r.getId() + ", " + r.getName() + ", " + r.getCapacity());
            }

    }
}
