package ca.utoronto.cs.service;

import ca.utoronto.cs.dao.IEventDao;
import ca.utoronto.cs.entity.Event;
import ca.utoronto.cs.entity.Room;
import ca.utoronto.cs.entity.EventType;
import ca.utoronto.cs.entity.User;
import ca.utoronto.cs.exception.EventConflictException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


public class EventManager {
	private final Logger logger = LogManager.getLogger();
	private IEventDao eventDao = null;

	public void setEventDao(IEventDao eventDao) {
		this.eventDao = eventDao;
	}

	public Event createEvent(String name, User organiser, Room room, List<User> speaker, Date start, Date end,
	                         EventType type, int capacity) {
		Event event = new Event();
		if(type.getValue() != 2){
			for(User u : speaker){
				event.getSpeakers().add(u.getId());
			}
		}
		event.setName(name);
		event.setOrganiserId(organiser.getId());
		event.setRoomId(room.getId());
		event.setStart(start);
		event.setEnd(end);
		event.setCapacity(capacity);
		event.setType(type);
		if (checkConflict(event))
			throw new EventConflictException("conflict");
		eventDao.save(event);
		return event;
	}

	public void delete(Event event){
		eventDao.delete(event);

	}

	/***
	 * Check if the proposed time slot conflicts with any existing events
	 * @param event2
	 * @return false if no conflicts
	 */
	private boolean checkConflict(Event event2) {
		for (Event event: eventDao.getAll()) {
			if ((event2.getStart().before(event.getEnd()) && event2.getStart().after(event.getStart())) ||
					(event2.getEnd().before(event.getEnd()) && event2.getEnd().after(event.getStart())) ||
					(event2.getStart().before(event.getStart()) && event2.getEnd().after(event.getEnd()))) {
				// event overlapped
				for (int u : event2.getSpeakers()){
					if (event.getSpeakers().contains(u)) {
						return true;
					}
				}
				if (event.getRoomId() == event2.getRoomId()){
					return true;}
			}
		}
		return false;
	}

	public Event getEventById(int id) {
		return eventDao.getById(id);
	}

	public List<Event> getAllEvents() {
		return eventDao.getAll();
	}

	public void save(Event event) {
		eventDao.save(event);
	}


	public void changeSpeaker(Event event, List<User> speakers){
		logger.debug("setting event {} to speaker {}", event, speakers);
		event.getSpeakers().clear();
		for(User sp : speakers) {
			event.getSpeakers().add(sp.getId());
		}
		if (checkConflict(event))
			throw new EventConflictException("conflict");
		eventDao.save(event);
	}

	/**
	 *
	 * @param speaker
	 * @return the list of event that has the speaker
	 */
	public List<Event> getEventsBySpeaker(User speaker){
		List<Event> eventsLst = new CopyOnWriteArrayList<>();
		for(Event e: eventDao.getAll()){
			if(e.getSpeakers().contains(speaker.getId()))
				eventsLst.add(e);
		}
		return eventsLst;
	}

}
