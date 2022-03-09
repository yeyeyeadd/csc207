package ca.utoronto.cs.responsehandler;

import ca.utoronto.cs.message.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ResponseHandlerFactory {
	private static final Logger logger = LogManager.getLogger();

	/**
	 * to create handler for each response
	 * @param type Response type.
	 * @return ResponseHandler
	 */
	public static ResponseHandler fromMessageType(MessageType type) {
		ResponseHandler handler;
		switch(type) {
			case BANNER:
				handler = new BannerResponseHandler(BannerResponse.class);
				break;
			case LOGIN_RESPONSE:
				handler = new LoginResponseHandler(LoginResponse.class);
				break;
			case LIST_EVENTS_RESPONSE:
				handler = new ListEventsResponseHandler(ListEventsResponse.class);
				break;
			case MESSAGE_FRIEND_RESPONSE:
				handler = new MessageFriendResponseHandler(MessageFriendResponse.class);
				break;
			case MESSAGE_RESPONSE:
				handler = new MessageResponseHandler(MessageResponse.class);
				break;
			case CREATE_EVENT_RESPONSE:
				handler = new CreateEventResponseHandler(CreateEventResponse.class);
				break;
			case DELETE_EVENT_RESPONSE:
				handler = new DeleteEventResponseHandler(DeleteEventResponse.class);
				break;
			case JOIN_EVENT_RESPONSE:
				handler = new JointEventResponseHandler(JoinEventResponse.class);
				break;
			case LEAVE_EVENT_RESPONSE:
				handler = new LeaveEventResponseHandler(LeaveEventResponse.class);
				break;
			case CREATE_USER_ACCOUNT_RESPONSE:
				handler = new CreateUserAccountResponseHandler(CreateUserAccountResponse.class);
				break;
			case CREATE_ROOM_RESPONSE:
				handler = new CreateRoomResponseHandler(CreateRoomResponse.class);
				break;
			case LIST_ROOMS_RESPONSE:
				handler = new ListRoomsResponseHandler(ListRoomsResponse.class);
				break;
			case DELETE_ROOM_RESPONSE:
				handler = new DeleteRoomResponseHandler(DeleteRoomResponse.class);
				break;
			default:
				logger.error("Unknown message type. type={}", type);
				handler = new DefaultResponseHandler(UnknownResponse.class);
		}
		return handler;
	}
}
