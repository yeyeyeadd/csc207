package ca.utoronto.cs.responsehandler;

import ca.utoronto.cs.message.LeaveEventResponse;
import io.netty.channel.ChannelHandlerContext;


public class LeaveEventResponseHandler  extends ResponseHandler<LeaveEventResponse>{
    public LeaveEventResponseHandler(Class<LeaveEventResponse> cls) {
        super(cls);
    }

    @Override
    public void _handle(ChannelHandlerContext ctx, LeaveEventResponse resp) {
        System.out.println(String.format("Leave event response: %s", resp));
    }
}