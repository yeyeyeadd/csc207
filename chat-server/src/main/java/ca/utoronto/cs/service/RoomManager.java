package ca.utoronto.cs.service;

import ca.utoronto.cs.dao.IRoomDao;
import ca.utoronto.cs.dao.RoomDao;
import ca.utoronto.cs.entity.Event;
import ca.utoronto.cs.entity.Room;
import ca.utoronto.cs.exception.GenericErrorException;
import ca.utoronto.cs.exception.ObjectNotFoundException;
import ca.utoronto.cs.exception.RoomAlreadyExistedExecption;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class RoomManager {
	private final Logger logger = LogManager.getLogger();
	private IRoomDao roomDao = null;

	public void setRoomDao(IRoomDao roomDao) {
		this.roomDao = roomDao;
	}

	/**
	 * return a room by giving room name.
	 * @param name room name
	 * @return a room by giving room name.
	 */
	public Room getRoom(String name) {
		if (roomDao.IsNameExist(name)) {
			return roomDao.getByName(name);
		}
		else {
			throw new ObjectNotFoundException(name);
		}
	}

	public Room createRoom(String name, int capacity) {
		if (!roomDao.IsNameExist(name)) {
			logger.info("Creating room{}", name);
			Room room = new Room();
			room.setName(name);
			room.setCapacity(capacity);
			roomDao.save(room);
			return room;
		}
		else{
			throw new RoomAlreadyExistedExecption(name);
		}
	}

	public Room getRoomById(int id) {
		return roomDao.getById(id);
	}

	/**
	 * return a list of room
	 * @return a list of room
	 */
	public List<Room> getAllRooms() {
		return roomDao.getAll();
	}

	public void delete(Room room){
		roomDao.delete(room);
	}
}
