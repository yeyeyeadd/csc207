package ca.utoronto.cs.dao;
import ca.utoronto.cs.entity.*;
import ca.utoronto.cs.exception.DatabaseException;
import ca.utoronto.cs.exception.ObjectNotFoundException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class RoomDaoSql extends DaoSql implements IRoomDao {
    private final Logger logger = LogManager.getLogger();
    private static final String TABLE = "rooms";

    public RoomDaoSql(Connection conn) {
        super(conn);
    }

    @Override
    public void save(Room obj) {
        String sql;
        if (obj.getId() > 0) {
            // update obj
            sql = "UPDATE " + TABLE + " SET name=?, capacity=?" +
                    " WHERE id=?";
        }
        else {
            sql = "INSERT INTO " + TABLE + " (name, capacity)" +
                    " VALUES (?,?)";
        }
        PreparedStatement pst = null;
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, obj.getName());
            pst.setInt(2, obj.getCapacity());
            if (obj.getId() > 0)
                pst.setInt(3,obj.getId());
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
    public Room getById(int id) {
        Room room= new Room();
        String sql = "SELECT * FROM " + TABLE + " WHERE id=?";
        PreparedStatement pst;
        try {
            pst = conn.prepareStatement(sql);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            room.setId(rs.getInt("id"));
            room.setName(rs.getString("name"));
            room.setCapacity(rs.getInt("capacity"));

        } catch (SQLException e) {
            logger.error(e);
            throw new ObjectNotFoundException(String.valueOf(id));
        }

        return room;
    }

    @Override
    public List<Room> getAll() {
        List<Room> allRoom = new CopyOnWriteArrayList<>();
        String sql = "SELECT * FROM " + TABLE;
        PreparedStatement pst;
        try {
            pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Room room = new Room();
                room.setId(rs.getInt("id"));
                room.setName(rs.getString("name"));
                room.setCapacity(rs.getInt("capacity"));
                allRoom.add(room);
            }
        }
        catch (SQLException e) {
            logger.error(e);
            throw new DatabaseException(e.toString());
        }
        return allRoom;

    }

    @Override
    public void delete(Room obj) {
        String sql = "DELETE FROM " + TABLE + " WHERE id=?";
        PreparedStatement pst;
        try {
            pst = conn.prepareStatement(sql);
            pst.setInt(1, obj.getId());
            pst.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
            throw new DatabaseException(e.toString());
        }
    }

    @Override
    public Room getByName(String name){
        Room room= new Room();
        String sql = "SELECT * FROM " + TABLE + " WHERE name=?";
        PreparedStatement pst = null;
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, name);
            ResultSet rs = pst.executeQuery();
            room.setId(rs.getInt("id"));
            room.setName(rs.getString("name"));
            room.setCapacity(rs.getInt("capacity"));

        } catch (SQLException e) {
            logger.error(e);
            throw new ObjectNotFoundException(String.valueOf(name));
        }

        return room;

    }
    public boolean IsNameExist(String name){
        String sql = "SELECT COUNT(id) FROM " + TABLE + " WHERE name=?";
        PreparedStatement pst;

        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, name);
            ResultSet rs = pst.executeQuery();
            return rs.getInt(1) > 0;
        } catch (SQLException e) {
            logger.error(e);
            throw new DatabaseException(e.toString());
        }
    }
}
