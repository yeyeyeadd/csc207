package ca.utoronto.cs.requesthandler;

import ca.utoronto.cs.exception.GenericErrorException;
import ca.utoronto.cs.exception.ObjectNotFoundException;
import ca.utoronto.cs.message.GenericErrorResponse;
import ca.utoronto.cs.message.LoginRequest;
import ca.utoronto.cs.message.LoginResponse;
import ca.utoronto.cs.entity.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoginRequestHandler extends RequestHandler<LoginRequest> {
	private final Logger logger = LogManager.getLogger();

	public LoginRequestHandler(Class<LoginRequest> cls) {
		super(cls);
	}

	/**
	 * To handle LoginRequest.
	 * 1.check if content is valid.
	 * 2.check if user id exist.
	 * 3.check password.
	 * 4.log in succeed
	 * @param ctx status
	 * @param loginMessage
	 */
	@Override
	public LoginResponse _handle(ChannelHandlerContext ctx, LoginRequest loginMessage) {
		LoginResponse resp = new LoginResponse();

		if (loginMessage == null || loginMessage.username == null || loginMessage.password == null) {
			throw new GenericErrorException("Invalid login request");
		}

		User user = null;
		try {
			user = userMgr.getUserByName(loginMessage.username);
		}
		catch (ObjectNotFoundException e) {
			throw new GenericErrorException("User not found");
		}

		if (!user.check(loginMessage.password)) {
			throw new GenericErrorException("Wrong password");
		}

		connMgr.switchUser(ctx.channel(), user);
		resp.user = user;
		return resp;
	}
}
