package ca.utoronto.cs.responsehandler;

import ca.utoronto.cs.message.DeleteRoomResponse;
import io.netty.channel.ChannelHandlerContext;

public class DeleteRoomResponseHandler  extends ResponseHandler<DeleteRoomResponse>{
    public DeleteRoomResponseHandler(Class<DeleteRoomResponse> cls) {
        super(cls);
    }

    @Override
    public void _handle(ChannelHandlerContext ctx, DeleteRoomResponse resp) {
        System.out.println(String.format("Received delete room response: %s", resp));
    }
}
