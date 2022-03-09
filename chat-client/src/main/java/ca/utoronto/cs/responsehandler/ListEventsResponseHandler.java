package ca.utoronto.cs.responsehandler;

import ca.utoronto.cs.entity.Event;
import ca.utoronto.cs.message.BannerResponse;
import ca.utoronto.cs.message.ListEventsResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class ListEventsResponseHandler extends ResponseHandler<ListEventsResponse> {
    private final Logger logger = LogManager.getLogger();

    public ListEventsResponseHandler(Class<ListEventsResponse> cls) {
        super(cls);
    }

    /**
     *To show the response of ListEventsRequest
     * @param ctx ctx
     * @param resp giving content
     */
    @Override
    public void _handle(ChannelHandlerContext ctx, ListEventsResponse resp) {
        System.out.print("All events: ");
        if (resp.events != null)
            for(Event e: resp.events){
                System.out.println(e.getId() + ", " + e.getName() + ", " + e.getSpeakers() + ", " + e.getRoomId()
                        + ", " + e.getStart() + ", " + e.getEnd());
            }

    }
}
