package ca.utoronto.cs.requesthandler;

import ca.utoronto.cs.message.*;
import ca.utoronto.cs.service.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * create handler for each request.
 */
public class RequestHandlerFactory {
	private static final Logger logger = LogManager.getLogger();

	public static RequestHandler fromMessageType(MessageType type) {
		RequestHandler handler;
		switch(type) {
			case LOGIN_REQUEST:
				handler = new LoginRequestHandler(LoginRequest.class);
				break;
			case CREATE_EVENT_REQUEST:
				handler = new CreateEventRequestHandler(CreateEventRequest.class);
				break;
			case JOIN_EVENT_REQUEST:
				handler = new JoinEventRequestHandler(JoinEventRequest.class);
				break;
			case LIST_EVENTS_REQUEST:
				handler = new ListEventsRequestHandler(ListEventsRequest.class);
				break;
			case SAVE_REQUEST:
				handler = new SaveRequestHandler(SaveRequest.class);
				break;
			case BROADCAST_AUDIENCES_REQUEST:
				handler = new BroadcastAudiencesRequestHandler(BroadcastAudiencesRequest.class);
				break;
			case MESSAGE_SPEAKER_REQUEST:
				handler = new MessageSpeakerRequestHandler(MessageSpeakerRequest.class);
				break;
			case MESSAGE_FRIEND_REQUEST:
				handler = new MessageFriendRequestHandler(MessageFriendRequest.class);
				break;
			case CREATE_USER_ACCOUNT_REQUEST:
				handler = new CreateUserAccountRequestHandler(CreateUserAccountRequest.class);
				break;
			case DELETE_EVENT_REQUEST:
				handler = new DeleteEventRequestHandler(DeleteEventRequest.class);
				break;
			case CHANGE_SPEAKER_REQUEST:
				handler = new ChangeSpeakerRequestHandler(ChangeSpeakerRequest.class);
				break;
			case LEAVE_EVENT_REQUEST:
				handler = new LeaveEventRequestHandler(LeaveEventRequest.class);
				break;
			case CHECK_ALL_MESSAGE_REQUEST:
				handler = new CheckAllMessageRequestHandler(CheckAllMessageRequest.class);
				break;
			case MESSAGE_EVENT_REQUEST:
				handler = new MessageEventRequestHandler(MessageEventRequest.class);
				break;
			case MESSAGE_ALL_SPEAKER_REQUEST:
				handler = new MessageAllSpeakerRequestHandler(MessageAllSpeakerRequest.class);
				break;
			case ADD_FRIEND_REQUEST:
				handler = new AddFriendRequestHandler(AddFriendRequest.class);
				break;
			case CREATE_ROOM_REQUEST:
				handler = new CreateRoomRequestHandler(CreateRoomRequest.class);
				break;
			case LIST_ROOMS_REQUEST:
				handler = new ListRoomsRequestHandler(ListRoomsRequest.class);
				break;
			case DELETE_ROOM_REQUEST:
				handler = new DeleteRoomRequestHandler(DeleteRoomRequest.class);
				break;
			case WHO_AM_I_REQUEST:
				handler = new WhoAmIRequestHandler(WhoAmIRequest.class);
				break;
			case GET_MESSAGE_REQUEST:
				handler = new GetMessageRequestHandler(GetMessageRequest.class);
				break;
			case CHANGE_EVENT_CAPACITY_REQUEST:
				handler = new ChangeEventCapacityRequestHandler(ChangeEventCapacityRequest.class);
				break;
			default:
				logger.error("Unknown message type. type={}", type);
				handler = new DefaultRequestHandler(UnknownRequest.class);
		}
		return handler;
	}
}
