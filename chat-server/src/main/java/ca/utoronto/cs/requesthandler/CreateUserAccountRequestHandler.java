package ca.utoronto.cs.requesthandler;

import ca.utoronto.cs.entity.Role;
import ca.utoronto.cs.entity.User;
import ca.utoronto.cs.exception.GenericErrorException;
import ca.utoronto.cs.exception.UserAlreadyExistedException;
import ca.utoronto.cs.message.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CreateUserAccountRequestHandler extends RequestHandler<CreateUserAccountRequest> {
    private final Logger logger = LogManager.getLogger();

    public CreateUserAccountRequestHandler(Class<CreateUserAccountRequest> cls) {
        super(cls);
    }

    /**
     * To handle CreateUserAccountRequest.
     * 1.check if content is valid.
     * 2.check user all to create this role of account.
     * 3.check the user name is exist.
     * 4.create user.
     * @param ctx status
     * @param req content from user.
     */
    @Override
    public CreateUserAccountResponse _handle(ChannelHandlerContext ctx, CreateUserAccountRequest req) {
        CreateUserAccountResponse resp = new CreateUserAccountResponse();

        User currUser = connMgr.getUserByChannel(ctx.channel());
        if(currUser.getRole().equals(Role.OPERATOR)){
            User user;
            try {
                if(req.role.equals(Role.USER))
                    user = userMgr.createUser(req.username, req.password, Role.USER);
                else if(req.role.equals(Role.OPERATOR))
                    user = userMgr.createUser(req.username, req.password, Role.OPERATOR);
                else if(req.role.equals(Role.VIP))
                    user = userMgr.createUser(req.username, req.password, Role.VIP);
                else{
                    throw new GenericErrorException("wrong role");
                }
            }
            catch (UserAlreadyExistedException e) {
                throw new GenericErrorException("user conflict");
            }
            logger.info("User created. user={}", user);
            resp.user = user;
            return resp;
//            throw new GenericErrorException("user conflict");
        }

        if(currUser.getRole().equals(Role.ANONYMOUS) && req.role.equals(Role.USER)){
            User user;
            try {
                user = userMgr.createUser(req.username, req.password, Role.USER);
            }
            catch (UserAlreadyExistedException e) {
                throw new GenericErrorException("user conflict");
            }
            logger.info("User created. user={}", user);
            resp.user = user;
            return resp;
        }
        throw new GenericErrorException("cannot create user");
    }
}
