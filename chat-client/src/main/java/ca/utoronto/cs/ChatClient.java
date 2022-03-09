package ca.utoronto.cs;

import ca.utoronto.cs.View.LoginFrame;
import ca.utoronto.cs.entity.Role;
import ca.utoronto.cs.entity.EventType;
import ca.utoronto.cs.message.*;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ChatClient {
	private final Logger logger = LogManager.getLogger();
	private String host;
	private int port;

	public ChatClient(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public void run() {
		logger.info("Connecting to {} port {}", host, port);
		RequestManager reqMgr = new RequestManager();
		try {
			reqMgr.connect(host, port);
		}
		catch (InterruptedException e) {
			logger.error("unable to connect to server");
			return;
		}
		reqMgr.subscribe(BannerResponse.class).subscribe(resp ->
			System.out.println(String.format("Banner: %s", resp.message)));


		SwingUtilities.invokeLater(() -> {
			LoginFrame frame = new LoginFrame(reqMgr);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);
		});

		try{
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			while(true) {
				String line = in.readLine();
				if (line.equals("exit"))
					break;
				else if (line.startsWith("login ")) {
					String[] params = line.split(" ");
					LoginRequest req = new LoginRequest();
					req.username = params[1];
					req.password = params[2];
					System.out.printf("logging in as %s\n", req.username);
					reqMgr.sendAsync(req)
							.thenAccept(System.out::println)
							.exceptionally(err -> {
								System.out.printf("err:%s%n\n", err);
								return null;
							});
				}
//				else if (line.startsWith("newevent ")) {
//					String[] params = line.split(" ");
//					CreateEventRequest req = new CreateEventRequest();
//					req.name = params[1];
//					req.room = params[2];
//					req.speaker = new ArrayList<>(Arrays.asList(params[3].split(",")));
//					req.startTime = params[4];
//					req.endTime = params[5];
//					if(params[6].equal("solo")){
//						req.type = TypeofEvent.SOLO;
//					}
//					else if (params[6].equals("none")){
//						req.type = TypeofEvent.NONE;
//					}
//					else if (params[6].equals("multiple")){
//						req.type = TypeofEvent.MULTIPLE;
//					}
//					else if (params[6].equals("vipevent")){
//						req.type = TypeofEvent.VIPEVENT;
//					}
//					req.type = params[6];
//					req.capacity = params[7];
//					logger.info("creating event,name={}, room={}, speaker={}, startTime={}, endTime={}, " +
//									"type={}, capacity={}",
//							req.name, req.room, req.speaker, req.startTime, req.endTime, req.type, req.capacity);
//					channel.writeAndFlush(req.putHeader());
//				}
//				else if (line.startsWith("join ")) {
//					String[] params = line.split(" ");
//					JoinEventRequest req = new JoinEventRequest();
//					req.eventId = Integer.parseInt(params[1]);
//					logger.info("joining event, id={}", req.eventId);
//					channel.writeAndFlush(req.putHeader());
//				}
//				else if (line.equals("listevents")) {
//					ListEventsRequest req = new ListEventsRequest();
//					logger.info("listing events");
//					channel.writeAndFlush(req.putHeader());
//				}
//				else if (line.startsWith("broadcast ")) {
//					String[] params = line.split(" ");
//					BroadcastAudiencesRequest req = new BroadcastAudiencesRequest();
//					req.eventId = Integer.parseInt(params[1]);
//					req.content = params[2];
//					channel.writeAndFlush(req.putHeader());
//				}
//				else if (line.startsWith("msgspeaker ")) {
//					String[] params = line.split(" ");
//					MessageSpeakerRequest req = new MessageSpeakerRequest();
//					req.eventId = Integer.parseInt(params[1]);
//					req.content = params[2];
//				}
//				else if (line.startsWith("messagefriend ")){
//					String[] params = line.split(" ", 3);
//					MessageFriendRequest req = new MessageFriendRequest();
//					req.receiver = params[1];
//					req.msg = params[2];
//					logger.info("creating a message");
//					channel.writeAndFlush(req.putHeader());
//				}
//				else if(line.startsWith("createuser ")){
//					String[] param = line.split(" ");
//					CreateUserAccountRequest req = new CreateUserAccountRequest();
//					req.username = param[1];
//					req.password = param[2];
//					if (param[3].equal("user")){
//						req.role = Role.USER;
//					}
//					else if(param[3].equal("operator")){
//						req.role = Role.OPERATOR;
//					}
//					else if(param[3].equal("vip")){
//						req.role = Role.VIP;
//					}
//					logger.info("creating an user account");
//					channel.writeAndFlush(req.putHeader());
//				}
//				else if(line.startsWith("deleteevent ")){
//					String[] param = line.split(" ");
//					DeleteEventRequest req = new DeleteEventRequest();
//					req.eventId = Integer.parseInt(param[1]);
//					logger.info("delete event");
//					channel.writeAndFlush(req.putHeader());
//				}
//				else if(line.startsWith("changespeaker ")){
//					String[] param = line.split(" ");
//					ChangeSpeakerRequest req = new ChangeSpeakerRequest();
//					req.eventId = Integer.parseInt(param[1]);
//					req.speakerName = new ArrayList<>(Arrays.asList(param[2].split(",")));
//					logger.info("change speaker");
//					channel.writeAndFlush(req.putHeader());
//				}
//				else if(line.startsWith("leave ")){
//					String[] param = line.split(" ");
//					LeaveEventRequest req = new LeaveEventRequest();
//					req.eventId = Integer.parseInt(param[1]);
//					logger.info("leave event");
//					channel.writeAndFlush(req.putHeader());
//				}
//				else if(line.equals("checkmsg")){
//					CheckAllMessageRequest req = new CheckAllMessageRequest();
//					logger.info("check all events");
//					channel.writeAndFlush(req.putHeader());
//				}
//				else if(line.startsWith("msgevent ")){
//					String[] param = line.split(" ", 3);
//					MessageEventRequest req = new MessageEventRequest();
//					req.eventId = Integer.parseInt(param[1]);
//					req.message = param[2];
//					logger.info("message to event");
//					channel.writeAndFlush(req.putHeader());
//				}
//				else if(line.startsWith("msgallspk ")){
//					String[] param = line.split(" ", 2);
//					MessageAllSpeakerRequest req = new MessageAllSpeakerRequest();
//					req.message = param[1];
//					logger.info("message all speaker");
//					channel.writeAndFlush(req.putHeader());
//				}
//				else if(line.startsWith("addfriend ")){
//					String[] param = line.split(" ");
//					AddFriendRequest req = new AddFriendRequest();
//					req.friendId = Integer.parseInt(param[1]);
//					logger.info("add a friend");
//					channel.writeAndFlush(req.putHeader());
//				}
//				else if(line.startsWith("newroom")){
//					String[] param = line.split(" ");
//					CreateRoomRequest req = new CreateRoomRequest();
//					req.id = new Integer(param[1]);
//					req.name = param[2];
//					req.capacity = new Interger(param[3]);
//					logger.info("create a room");
//					channel.writeAndFlush(req.putHeader);
//				}
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}

	}
}
