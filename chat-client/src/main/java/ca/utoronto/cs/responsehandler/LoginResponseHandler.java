package ca.utoronto.cs.responsehandler;

import ca.utoronto.cs.message.LoginResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoginResponseHandler extends ResponseHandler<LoginResponse> {
	private final Logger logger = LogManager.getLogger();

	public LoginResponseHandler(Class<LoginResponse> cls) {
		super(cls);
	}

	/**
	 *To show the response of LoginRequest
	 * @param ctx ctx
	 * @param resp giving content
	 */
	@Override
	public void _handle(ChannelHandlerContext ctx, LoginResponse resp) {
		System.out.println(String.format("Received login response: %s", resp));
	}
}
