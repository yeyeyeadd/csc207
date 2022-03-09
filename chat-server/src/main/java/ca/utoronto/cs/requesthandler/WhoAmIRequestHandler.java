package ca.utoronto.cs.requesthandler;

import ca.utoronto.cs.entity.User;
import ca.utoronto.cs.message.Request;
import ca.utoronto.cs.message.Response;
import ca.utoronto.cs.message.WhoAMIResponse;
import ca.utoronto.cs.message.WhoAmIRequest;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WhoAmIRequestHandler extends RequestHandler<WhoAmIRequest>{
    private final Logger logger = LogManager.getLogger();

    public WhoAmIRequestHandler(Class<WhoAmIRequest> cls) {
        super(cls);
    }

    @Override
    protected WhoAMIResponse _handle(ChannelHandlerContext ctx, WhoAmIRequest req) {
        WhoAMIResponse resp = new WhoAMIResponse();

        logger.info("give the user information");
        User currUser;
        currUser = connMgr.getUserByChannel(ctx.channel());
        resp.user = currUser;
        return resp;
    }
}
