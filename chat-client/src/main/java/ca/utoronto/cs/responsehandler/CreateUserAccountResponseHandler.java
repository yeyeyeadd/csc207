package ca.utoronto.cs.responsehandler;

import ca.utoronto.cs.message.CreateUserAccountResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CreateUserAccountResponseHandler extends ResponseHandler<CreateUserAccountResponse> {
    private final Logger logger = LogManager.getLogger();

    public CreateUserAccountResponseHandler(Class<CreateUserAccountResponse> cls) {
        super(cls);
    }

    /**
     *To show the response of CreateUserAccountRequest
     * @param ctx ctx
     * @param resp giving content
     */
    @Override
    public void _handle(ChannelHandlerContext ctx, CreateUserAccountResponse resp) {
        System.out.println(String.format("Received create user account response: %s", resp));
    }
}
