package ca.utoronto.cs.responsehandler;

import ca.utoronto.cs.message.JoinEventResponse;
import io.netty.channel.ChannelHandlerContext;

public class JointEventResponseHandler  extends ResponseHandler<JoinEventResponse>{
    public JointEventResponseHandler(Class<JoinEventResponse> cls) {
        super(cls);
    }

    @Override
    public void _handle(ChannelHandlerContext ctx, JoinEventResponse resp) {
        System.out.println(String.format("Joint event response: %s", resp));
    }
}