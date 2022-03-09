package ca.utoronto.cs.dao;

import ca.utoronto.cs.entity.Room;

public interface IRoomDao extends DataAccessObject<Room> {
	Room getByName(String name);

	boolean IsNameExist(String name);
}
