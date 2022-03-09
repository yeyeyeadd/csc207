package ca.utoronto.cs;

import ca.utoronto.cs.dao.*;
import ca.utoronto.cs.service.*;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ChatServer {
    private final static Logger logger = LogManager.getLogger(ChatServer.class);
    private final int PORT = 5556;

    private final ConnectionManager connMgr = new ConnectionManager();
    private final EventManager eventMgr = new EventManager();
    private final RoomManager roomMgr = new RoomManager();
    private final UserManager userMgr = new UserManager();
    private final MessageManager messageMgr = new MessageManager();

    public void run() {
        logger.info("Starting...");

        IEventDao eventDao = new EventDao();
        IRoomDao roomDao = new RoomDao();
        IMessageDao messageDao = new MessageDao();
        IUserDao userDao = new UserDao();

        Connection conn = null;
        String dbUri = "jdbc:sqlite:devdata.sqlite";
        try {
            conn = DriverManager.getConnection(dbUri);
        } catch (SQLException e) {
            logger.error("unable to connect to database");
            logger.error(e);
        }
//        IEventDao eventDao = new EventDaoSql(conn);
//        IRoomDao roomDao = new RoomDaoSql(conn);
//        IMessageDao messageDao = new MessageDaoSql(conn);
//        IUserDao userDao = new UserDaoSql(conn);

        eventMgr.setEventDao(eventDao);
        roomMgr.setRoomDao(roomDao);
        userMgr.setUserDao(userDao);
        messageMgr.setMessageDao(messageDao);

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap()
                    .group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChatServerInitialiser(
                            connMgr, eventMgr, roomMgr, userMgr, messageMgr)
                    );
            logger.info("Listen on {}", PORT);
            ChannelFuture future = bootstrap.bind(PORT).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            logger.info("Shutting down...");
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}
