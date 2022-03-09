package ca.utoronto.cs.requesthandler;

import ca.utoronto.cs.entity.Role;
import ca.utoronto.cs.entity.User;
import ca.utoronto.cs.exception.GenericErrorException;
import ca.utoronto.cs.exception.ObjectNotFoundException;
import ca.utoronto.cs.message.AddFriendRequest;
import ca.utoronto.cs.message.AddFriendResponse;
import ca.utoronto.cs.message.CreateEventRequest;
import ca.utoronto.cs.message.GenericErrorResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AddFriendRequestHandler extends RequestHandler<AddFriendRequest> {
    private final Logger logger = LogManager.getLogger();

    public AddFriendRequestHandler(Class<AddFriendRequest> cls) {
        super(cls);
    }

    /**
     * This handle will handle add friend request.
     * 1. check if content is valid.
     * 2. check if userid exist.
     * 3. check if user is allow to add this friend.
     * 4. add friend.
     * @param ctx status
     * @param req content from user.
     */
    @Override
    public AddFriendResponse _handle(ChannelHandlerContext ctx, AddFriendRequest req) {
        AddFriendResponse resp = new AddFriendResponse();

        User currUser = connMgr.getUserByChannel(ctx.channel());
        if(currUser.getRole().equals(Role.ANONYMOUS)){
            throw new GenericErrorException("anonymous user is forbidden");
        }

        User friend;
        try{
            friend = userMgr.getUserById(req.friendId);
        }
        catch (ObjectNotFoundException e){
            throw new GenericErrorException("unable to find friend");
        }

        if(currUser.getFriends().contains(req.friendId)){
            throw new GenericErrorException("users are already friends");
        }
        else if(currUser.equals(friend)){
            throw new GenericErrorException("cannot add yourself as a friend");
        }

        currUser.addFriendToFriends(friend.getId());
        friend.addFriendToFriends(currUser.getId());
        resp.userA = currUser;
        resp.userB = friend;
        userMgr.save(currUser);
        userMgr.save(friend);
        return resp;
    }
}
