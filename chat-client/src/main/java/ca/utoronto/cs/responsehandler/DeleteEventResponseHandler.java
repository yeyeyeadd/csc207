package ca.utoronto.cs.responsehandler;

import ca.utoronto.cs.message.DeleteEventResponse;
import io.netty.channel.ChannelHandlerContext;

public class DeleteEventResponseHandler extends ResponseHandler<DeleteEventResponse>{
    public DeleteEventResponseHandler(Class<DeleteEventResponse> cls) {
        super(cls);
    }

    @Override
    public void _handle(ChannelHandlerContext ctx, DeleteEventResponse resp) {
        System.out.println(String.format("Received delete event response: %s", resp));
    }
}
