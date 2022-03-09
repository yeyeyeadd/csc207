package ca.utoronto.cs.dao;

import ca.utoronto.cs.entity.Event;

import java.util.List;

public interface IEventDao extends DataAccessObject<Event> {

	Event getByName(String name);

	boolean IsNameExist(String name);
}
