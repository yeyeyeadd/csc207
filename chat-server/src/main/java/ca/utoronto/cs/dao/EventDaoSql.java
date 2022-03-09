package ca.utoronto.cs.dao;

import ca.utoronto.cs.entity.Event;
import ca.utoronto.cs.entity.EventType;
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

public class EventDaoSql extends DaoSql implements IEventDao {
	private final Logger logger = LogManager.getLogger();
	private static final String TABLE = "events";
	private static final String SPEAKERTABLE = "speakers";
	private static final String PARTICIPANTTABLE = "participants";


	public EventDaoSql(Connection conn) {
		super(conn);
	}

	@Override
	public void save(Event obj) {
		String sql;
		if (obj.getId() > 0) {
			// update obj
			sql = "UPDATE " + TABLE + " SET name=?, organiserId=?," +
					" roomId=?, start=?, end=?, type=?, capacity=?" +
					" WHERE id=?";
		}
		else {
			sql = "INSERT INTO " + TABLE + " (name, organiserId," +
					" roomId, start, end, type, capacity)" +
					" VALUES (?,?,?,?,?,?,?)";
		}
		PreparedStatement pst = null;
		try {
			pst = conn.prepareStatement(sql);
			pst.setString(1, obj.getName());
			pst.setInt(2, obj.getOrganiserId());
			pst.setInt(3, obj.getRoomId());
			pst.setLong(4, obj.getStart().getTime());
			pst.setLong(5, obj.getEnd().getTime());
			pst.setInt(6, obj.getType().getValue());
			pst.setInt(7, obj.getCapacity());
			if (obj.getId() > 0)
				pst.setInt(8, obj.getId());
			pst.executeUpdate();
			if (!(obj.getId() > 0)) {
				ResultSet rs = pst.getGeneratedKeys();
				obj.setId(rs.getInt(1));
			}
		} catch (SQLException e) {
			logger.error(e);
			throw new DatabaseException(e.toString());
		}

		// remove all speakers first
		if (obj.getId() > 0) {
			sql = "DELETE FROM " + SPEAKERTABLE + " WHERE eventId=?";
			try {
				pst = conn.prepareStatement(sql);
				pst.setInt(1, obj.getId());
				pst.executeUpdate();
			} catch (SQLException e) {
				logger.error(e);
				throw new DatabaseException(e.toString());
			}
		}

		// add all speakers
		for (int speaker: obj.getSpeakers()) {
			sql = "INSERT INTO " + SPEAKERTABLE + " (eventId, userId) VALUES (?, ?)";
			try {
				pst = conn.prepareStatement(sql);
				pst.setInt(1, obj.getId());
				pst.setInt(2, speaker);
				pst.executeUpdate();
			} catch (SQLException e) {
				logger.error(e);
				throw new DatabaseException(e.toString());
			}
		}

		// remove all participants first
		if (obj.getId() > 0) {
			sql = "DELETE FROM " + PARTICIPANTTABLE + " WHERE eventId=?";
			try {
				pst = conn.prepareStatement(sql);
				pst.setInt(1, obj.getId());
				pst.executeUpdate();
			} catch (SQLException e) {
				logger.error(e);
				throw new DatabaseException(e.toString());
			}
		}

		// add all participants
		for (int participant: obj.getParticipants()) {
			sql = "INSERT INTO " + PARTICIPANTTABLE + " (eventId, userId) VALUES (?, ?)";
			try {
				pst = conn.prepareStatement(sql);
				pst.setInt(1, obj.getId());
				pst.setInt(2, participant);
				pst.executeUpdate();
			} catch (SQLException e) {
				logger.error(e);
				throw new DatabaseException(e.toString());
			}
		}
	}

	@Override
	public Event getById(int id) {
		Event event = new Event();
		String sql = "SELECT * FROM " + TABLE + " WHERE id=?";
		PreparedStatement pst = null;
		try {
			pst = conn.prepareStatement(sql);
			pst.setInt(1, id);
			ResultSet rs = pst.executeQuery();
			event.setId(rs.getInt("id"));
			event.setName(rs.getString("name"));
			event.setOrganiserId(rs.getInt("organiserId"));
			event.setRoomId(rs.getInt("roomId"));
			event.setStart(new Date(rs.getLong("start")));
			event.setEnd(new Date(rs.getLong("end")));
			event.setType(EventType.fromValue(rs.getInt("type")));
			event.setCapacity(rs.getInt("capacity"));

			String sqlSpeakers = "SELECT * FROM " + SPEAKERTABLE + " WHERE eventId=?";
			pst = conn.prepareStatement(sqlSpeakers);
			pst.setInt(1, event.getId());
			ResultSet rsSpeakers = pst.executeQuery();
			List<Integer> speakers = new CopyOnWriteArrayList<>();
			while(rsSpeakers.next()) {
				speakers.add(rsSpeakers.getInt("userId"));
			}
			event.setSpeakers(speakers);

			String sqlParticipants = "SELECT * FROM " + PARTICIPANTTABLE + " WHERE eventId=?";
			pst = conn.prepareStatement(sqlParticipants);
			pst.setInt(1, event.getId());
			ResultSet rsParticipants = pst.executeQuery();
			List<Integer> participants = new CopyOnWriteArrayList<>();
			while (rsParticipants.next()) {
				participants.add(rsParticipants.getInt("userId"));
			}
			event.setParticipants(participants);
		} catch (SQLException e) {
			logger.error(e);
			throw new ObjectNotFoundException(String.valueOf(id));
		}

		return event;
	}

	@Override
	public List<Event> getAll() {
		List<Event> allevents = new CopyOnWriteArrayList<>();
		String sql = "SELECT * FROM " + TABLE;
		PreparedStatement pst = null;
		try {
			pst = conn.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			while(rs.next()) {
				Event event = new Event();
				event.setId(rs.getInt("id"));
				event.setName(rs.getString("name"));
				event.setOrganiserId(rs.getInt("organiserId"));
				event.setRoomId(rs.getInt("roomId"));
				event.setStart(new Date(rs.getLong("start")));
				event.setEnd(new Date(rs.getLong("end")));
				event.setType(EventType.fromValue(rs.getInt("type")));
				event.setCapacity(rs.getInt("capacity"));

				String sqlSpeakers = "SELECT * FROM " + SPEAKERTABLE + " WHERE eventId=?";
				pst = conn.prepareStatement(sqlSpeakers);
				pst.setInt(1, event.getId());
				ResultSet rsSpeakers = pst.executeQuery();
				List<Integer> speakers = new CopyOnWriteArrayList<>();
				while(rsSpeakers.next()) {
					speakers.add(rsSpeakers.getInt("userId"));
				}
				event.setSpeakers(speakers);

				String sqlParticipants = "SELECT * FROM " + PARTICIPANTTABLE + " WHERE eventId=?";
				pst = conn.prepareStatement(sqlParticipants);
				pst.setInt(1, event.getId());
				ResultSet rsParticipants = pst.executeQuery();
				List<Integer> participants = new CopyOnWriteArrayList<>();
				while (rsParticipants.next()) {
					participants.add(rsParticipants.getInt("userId"));
				}
				event.setParticipants(participants);
				allevents.add(event);
			}
		} catch (SQLException e) {
			logger.error(e);
			throw new DatabaseException(e.toString());
		}
		return allevents;
	}

	@Override
	public void delete(Event obj) {
		String sql = "DELETE FROM " + TABLE + " WHERE id=?";
		PreparedStatement pst = null;
		try {
			pst = conn.prepareStatement(sql);
			pst.setInt(1, obj.getId());
			pst.executeUpdate();
		} catch (SQLException e) {
			logger.error(e);
			throw new DatabaseException(e.toString());
		}

		sql = "DELETE FROM " + SPEAKERTABLE + " WHERE eventId=?";
		try {
			pst = conn.prepareStatement(sql);
			pst.setInt(1, obj.getId());
			pst.executeUpdate();
		} catch (SQLException e) {
			logger.error(e);
			throw new DatabaseException(e.toString());
		}

		sql = "DELETE FROM " + PARTICIPANTTABLE + " WHERE eventId=?";
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
	public Event getByName(String name) {
		Event event = new Event();
		String sql = "SELECT * FROM " + TABLE + " WHERE name=?";
		PreparedStatement pst = null;
		try {
			pst = conn.prepareStatement(sql);
			pst.setString(1, name);
			ResultSet rs = pst.executeQuery();
			event.setId(rs.getInt("id"));
			event.setName(rs.getString("name"));
			event.setOrganiserId(rs.getInt("organiserId"));
			event.setRoomId(rs.getInt("roomId"));
			event.setStart(new Date(rs.getLong("start")));
			event.setEnd(new Date(rs.getLong("end")));
			event.setType(EventType.fromValue(rs.getInt("type")));
			event.setCapacity(rs.getInt("capacity"));

			String sqlSpeakers = "SELECT * FROM " + SPEAKERTABLE + " WHERE eventId=?";
			pst = conn.prepareStatement(sqlSpeakers);
			ResultSet rsSpeakers = pst.executeQuery();
			List<Integer> speakers = new CopyOnWriteArrayList<>();
			while(rsSpeakers.next()) {
				speakers.add(rsSpeakers.getInt("userId"));
			}
			event.setSpeakers(speakers);

			String sqlParticipants = "SELECT * FROM " + PARTICIPANTTABLE + " WHERE eventId=?";
			pst = conn.prepareStatement(sqlParticipants);
			ResultSet rsParticipants = pst.executeQuery();
			List<Integer> participants = new CopyOnWriteArrayList<>();
			while (rsParticipants.next()) {
				participants.add(rsParticipants.getInt("userId"));
			}
		} catch (SQLException e) {
			logger.error(e);
			throw new ObjectNotFoundException(name);
		}
		return event;
	}

	@Override
	public boolean IsNameExist(String name) {
		String sql = "SELECT COUNT(id) FROM " + TABLE + " WHERE name=?";
		PreparedStatement pst = null;

		try {
			pst = conn.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			return rs.getInt(1) > 0;
		} catch (SQLException e) {
			logger.error(e);
			throw new ObjectNotFoundException(name);
		}
	}
}
