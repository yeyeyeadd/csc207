package ca.utoronto.cs.dao;

import ca.utoronto.cs.entity.Message;

import java.util.List;

public interface IMessageDao extends DataAccessObject<Message> {
	List<Message> getByEventId(int eventId);
}
