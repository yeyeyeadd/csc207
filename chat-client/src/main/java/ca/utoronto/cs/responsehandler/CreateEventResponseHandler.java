package ca.utoronto.cs.responsehandler;

import ca.utoronto.cs.message.BannerResponse;
import ca.utoronto.cs.message.CreateEventResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CreateEventResponseHandler extends ResponseHandler<CreateEventResponse> {
    private final Logger logger = LogManager.getLogger();

    public CreateEventResponseHandler(Class<CreateEventResponse> cls) {
        super(cls);
    }

    /**
     *To show the response of CreateEventRequest
     * @param ctx ctx
     * @param resp giving content
     */
    @Override
    public void _handle(ChannelHandlerContext ctx, CreateEventResponse resp) {
        System.out.println(String.format("Received create event response: %s", resp));
    }
}
