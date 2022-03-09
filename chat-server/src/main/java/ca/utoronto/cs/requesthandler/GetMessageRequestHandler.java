package ca.utoronto.cs.requesthandler;

import ca.utoronto.cs.entity.Message;
import ca.utoronto.cs.entity.User;
import ca.utoronto.cs.exception.GenericErrorException;
import ca.utoronto.cs.message.GetMessageRequest;
import ca.utoronto.cs.message.GetMessageResponse;
import ca.utoronto.cs.message.Response;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class GetMessageRequestHandler extends RequestHandler<GetMessageRequest>{
    private final Logger logger = LogManager.getLogger();
    public GetMessageRequestHandler(Class<GetMessageRequest> cls) {
        super(cls);
    }

    @Override
    protected Response _handle(ChannelHandlerContext ctx, GetMessageRequest req) {
        GetMessageResponse resp = new GetMessageResponse();
        User currUser;
        currUser = connMgr.getUserByChannel(ctx.channel());
        List<Message> message;
        message = msgMgr.getMessagesByUserId(currUser.getId());
        List<Message> myMessage = new ArrayList<>();
        if (req.type.equals("event")){
            for (Message m : message){
                if (m.getEventId() != null ){
                    myMessage.add(m);
                }
            }
            logger.info("print all event message");
            resp.messages = myMessage;
            return resp;
        }


        else if (req.type.equals("friend")){
            for (Message m : message){
                if (m.getEventId() == null){
                    myMessage.add(m);
                }
            }
            logger.info("print all friends message");
            resp.messages = myMessage;
            return resp;
        }
        else {
            logger.info("wrong type");
            throw new GenericErrorException("wrong type");
        }
    }


}

