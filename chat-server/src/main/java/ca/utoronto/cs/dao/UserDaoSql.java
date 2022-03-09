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

public class UserDaoSql extends DaoSql implements IUserDao{
    private final Logger logger = LogManager.getLogger();
    private static final String TABLE = "users";
    private static final String FRIENDTABLE = "friends";


    public UserDaoSql(Connection conn) {
        super(conn);
    }

    @Override
    public void save(User obj) {
        String sql;
        if (obj.getId() > 0) {
            // update obj
            sql = "UPDATE " + TABLE + " SET username=?, password=?, role=?" +
                    " WHERE id=?";
        }
        else {
            sql = "INSERT INTO " + TABLE + " (username, password, role)" +
                    " VALUES (?,?,?)";
        }
        PreparedStatement pst;
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, obj.getUsername());
            pst.setString(2, obj.getPassword());
            pst.setInt(3, obj.getRole().getValue());
            if (obj.getId() > 0)
                pst.setInt(4, obj.getId());
            pst.executeUpdate();
            if (!(obj.getId() > 0)) {
                ResultSet rs = pst.getGeneratedKeys();
                obj.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            logger.error(e);
            throw new DatabaseException(e.toString());
        }

        // when updating existing user, remove all friends first
        if (obj.getId() > 0) {
            sql = "DELETE FROM " + FRIENDTABLE + " WHERE userId=?";
            try {
                pst = conn.prepareStatement(sql);
                pst.setInt(1, obj.getId());
            } catch (SQLException e) {
                logger.error(e);
                throw new DatabaseException(e.toString());
            }
        }

        // insert friends
        for (int friendId: obj.getFriends()) {
            sql = "INSERT INTO " + FRIENDTABLE + "(userId, friendId) VALUES (?, ?)";
            try {
                pst = conn.prepareStatement(sql);
                pst.setInt(1, obj.getId());
                pst.setInt(2, friendId);
                pst.executeUpdate();
            } catch (SQLException e) {
                logger.error(e);
                throw new DatabaseException(e.toString());
            }
        }
    }

    @Override
    public User getById(int id) {
        User user= new User();
        String sql = "SELECT * FROM " + TABLE + " WHERE id=?";
        PreparedStatement pst;
        try {
            pst = conn.prepareStatement(sql);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            user.setId(rs.getInt("id"));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            user.setRole(Role.fromValue(rs.getInt("role")));
        } catch (SQLException e) {
            logger.error(e);
            throw new ObjectNotFoundException(String.valueOf(id));
        }

        sql = "SELECT * FROM " + FRIENDTABLE + " WHERE userId=?";
        try {
            pst = conn.prepareStatement(sql);
            pst.setInt(1, id);
            List<Integer> friends = new CopyOnWriteArrayList<>();
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                friends.add(rs.getInt("friendId"));
            }
            user.setFriends(friends);
        }
        catch (SQLException e) {
            logger.error(e);
            throw new ObjectNotFoundException(String.valueOf(id));
        }

        return user;
    }

    @Override
    public List<User> getAll() {
        List<User> allUser = new CopyOnWriteArrayList<>();
        String sql = "SELECT * FROM " + TABLE;
        PreparedStatement pst;
        try {
            pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));

                String sqlFriends = "SELECT * FROM " + FRIENDTABLE + " WHERE userId=?";
                pst = conn.prepareStatement(sqlFriends);
                pst.setInt(1, user.getId());
                ResultSet rsFriends = pst.executeQuery();
                List<Integer> friends = new CopyOnWriteArrayList<>();
                while (rsFriends.next()) {
                    friends.add(rsFriends.getInt("friendId"));
                }
                user.setFriends(friends);

                allUser.add(user);
            }
        }
        catch (SQLException e) {
            logger.error(e);
            throw new DatabaseException(e.toString());
        }
        return allUser;
    }

    @Override
    public void delete(User obj) {
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

        sql = "DELETE FROM " + FRIENDTABLE + " WHERE userId=?";
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
    public User getByName(String username) {
        User user= new User();
        String sql = "SELECT * FROM " + TABLE + " WHERE username=?";
        PreparedStatement pst;
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, username);
            ResultSet rs = pst.executeQuery();
            user.setId(rs.getInt("id"));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            user.setRole(Role.fromValue(rs.getInt("role")));

            String sqlFriends = "SELECT * FROM " + FRIENDTABLE + " WHERE userId=?";
            pst = conn.prepareStatement(sqlFriends);
            pst.setInt(1, user.getId());
            ResultSet rsFriends = pst.executeQuery();
            List<Integer> friends = new CopyOnWriteArrayList<>();
            while (rsFriends.next()) {
                friends.add(rsFriends.getInt("friendId"));
            }
            user.setFriends(friends);

        } catch (SQLException e) {
            logger.error(e);
            throw new ObjectNotFoundException(String.valueOf(username));
        }

        return user;
    }

    public boolean IsNameExist(String username){
        String sql = "SELECT COUNT(id) FROM " + TABLE + " WHERE username=?";
        PreparedStatement pst;

        try {
            pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            return rs.getInt(1) > 0;
        } catch (SQLException e) {
            logger.error(e);
            throw new DatabaseException(e.toString());
        }
    }

    @Override
    public long getUserCount() {
        String sql = "SELECT COUNT(id) FROM " + TABLE;
        PreparedStatement pst;
        try {
            pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            return rs.getInt(1);
        } catch (SQLException e) {
            logger.error(e);
            throw new DatabaseException(e.toString());
        }
    }

}

