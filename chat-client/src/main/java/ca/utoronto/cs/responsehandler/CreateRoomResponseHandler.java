package ca.utoronto.cs.responsehandler;
import ca.utoronto.cs.message.CreateRoomResponse;
import ca.utoronto.cs.message.CreateUserAccountResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CreateRoomResponseHandler extends ResponseHandler<CreateRoomResponse>{
	private final Logger logger = LogManager.getLogger();
	
    public CreateRoomResponseHandler(Class<CreateRoomResponse> cls) {
        super(cls);
    }
    
    @Override
    public void _handle(ChannelHandlerContext ctx, CreateRoomResponse resp) {
        System.out.println(String.format("Received create room response: %s", resp));
    }
}

