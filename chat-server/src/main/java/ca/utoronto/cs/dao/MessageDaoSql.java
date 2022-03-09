package ca.utoronto.cs.dao;

import ca.utoronto.cs.entity.Message;
import ca.utoronto.cs.exception.DatabaseException;
import ca.utoronto.cs.exception.ObjectNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MessageDaoSql extends DaoSql implements IMessageDao{
    private final Logger logger = LogManager.getLogger();
    private static final String TABLE = "messages";

    public MessageDaoSql(Connection conn) {
        super(conn);
    }

    @Override
    public void save(Message obj) {
        String sql;
        if (obj.getId() > 0) {
            // update obj
            sql = "UPDATE " + TABLE + " SET fromUserId=?, toUserId=?," +
                    " from_alias=?, time=?, content=?, eventId=?" +
                    " WHERE id=?";
        }
        else {
            sql = "INSERT INTO " + TABLE + " (fromUserId, toUserId," +
                    " from_alias, time, content, eventId)" +
                    " VALUES (?,?,?,?,?,?)";
        }
        PreparedStatement pst = null;
        try {
            pst = conn.prepareStatement(sql);
            pst.setInt(1, obj.getFromUserId());
            pst.setInt(2, obj.getToUserId());
            pst.setString(3, obj.getFrom_alias());
            pst.setLong(4, obj.getTime().getTime());
            pst.setString(5, obj.getContent());
            pst.setObject(6, obj.getEventId());
            if (obj.getId() > 0)
                pst.setInt(7,obj.getId());
            pst.executeUpdate();
            if (!(obj.getId() > 0)) {
                ResultSet rs = pst.getGeneratedKeys();
                obj.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            logger.error(e);
            throw new DatabaseException(e.toString());
        }
    }

    @Override
    public Message getById(int id) {
        Message message= new Message();
        String sql = "SELECT * FROM " + TABLE + " WHERE id=?";
        PreparedStatement pst = null;
        try {
            pst = conn.prepareStatement(sql);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            message.setId(rs.getInt("id"));
            message.setFromUserId(rs.getInt("fromUserId"));
            message.setToUserId(rs.getInt("toUserId"));
            message.setFrom_alias(rs.getString("from_alias"));
            message.setTime(new Date(rs.getLong("time")));
            message.setContent(rs.getString("content"));
            message.setEventId(rs.getInt("eventId"));
        } catch (SQLException e) {
            logger.error(e);
            throw new ObjectNotFoundException(String.valueOf(id));
        }

        return message;
    }

    @Override
    public List<Message> getAll() {
        List<Message> allmessage = new CopyOnWriteArrayList<>();
        String sql = "SELECT * FROM " + TABLE;
        PreparedStatement pst;
        try {
            pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Message message = new Message();
                message.setId(rs.getInt("id"));
                message.setTime(new Date(rs.getInt("time")));
                message.setEventId(rs.getInt("eventId"));
                message.setContent(rs.getString("content"));
                message.setFrom_alias(rs.getString("from_alias"));
                message.setFromUserId(rs.getInt("fromUserId"));
                message.setToUserId(rs.getInt("toUserId"));
                allmessage.add(message);
            }
        }
        catch (SQLException e) {
            logger.error(e);
            throw new DatabaseException(e.toString());
        }
        return allmessage;
    }

    @Override
    public void delete(Message obj) {
        String sql = "DELETE FROM " + TABLE + " WHERE id=?";
        PreparedStatement pst;
        try {
            pst = conn.prepareStatement(sql);
            pst.setInt(1, obj.getId());
            pst.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
            throw new ObjectNotFoundException(String.valueOf(obj.getId()));
        }
    }

    @Override
    public List<Message> getByEventId(int eventId) {
        List<Message> allmessage = new CopyOnWriteArrayList<>();
        String sql = "SELECT * FROM " + TABLE + " WHERE eventId=?";
        PreparedStatement pst = null;
        try {
            pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Message message = new Message();
                message.setId(rs.getInt("id"));
                if(message.getId() == eventId){
                    allmessage.add(message);
                }
            }
        }
        catch (SQLException e) {
            logger.error(e);
            throw new ObjectNotFoundException("getAll");
        }
        return allmessage;
    }
}
