package ca.utoronto.cs.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Event {
    private List<Integer> participants = new CopyOnWriteArrayList<>();
    private int id;
    private String name;
    private int organiserId;
    private int roomId;
    private List<Integer> speakers = new CopyOnWriteArrayList<>();
    private Date start;
    private Date end;
    private EventType type;
    private int capacity;
    /**
     *Return participants of Event
     * @return the participants for user who send message.
     */
    public List<Integer> getParticipants() {
        return participants;
    }
    /**
     * setter for participants
     * @param participants the content of user send.
     */
    public void setParticipants(List<Integer> participants) {
        this.participants = participants;
    }
    /**
     * Return the id of this event
     *
     * @return the id of this event
     */
    public int getId() {
        return id;
    }
    /**
     * set the id of this event
     *
     * @param id the id of this event
     */
    public void setId(int id) {
        this.id = id;
    }
    /**
     * Return the name of this event
     *
     * @return the name of this event
     */
    public String getName() {
        return name;
    }
    /**
     * set the name of this event
     *
     * @param name the name of this event
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * Return the organiserId of this event
     *
     * @return the organiserId of this event
     */
    public int getOrganiserId() {
        return organiserId;
    }
    /**
     * set the organiserId of this event
     *
     * @param organiserId the organiserId of this event
     */
    public void setOrganiserId(int organiserId) {
        this.organiserId = organiserId;
    }
    /**
     * Return the roomId of this event
     *
     * @return the roomId of this event
     */
    public int getRoomId() {
        return roomId;
    }
    /**
     * set the roomId of this event
     *
     * @param roomId the roomId of this event
     */
    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }
    /**
     * Return the speakerId of this event
     *
     * @return the speakerId of this event
     */
    public List<Integer> getSpeakers() {
        return speakers;
    }
    /**
     * set the speakerId of this event
     *
     * @param speakers the speakerId of this event
     */
    public void setSpeakers(List<Integer> speakers) {
        this.speakers = speakers;
    }

    /**
     * Return the start of this event
     *
     * @return the start of this event
     */
    public Date getStart() {
        return start;
    }
    /**
     * set the start of this event
     *
     * @param start the start of this event
     */
    public void setStart(Date start) {
        this.start = start;
    }
    /**
     * Return the end of this event
     *
     * @return the end of this event
     */
    public Date getEnd() {
        return end;
    }
    /**
     * set the end of this event
     *
     * @param end the end of this event
     */
    public void setEnd(Date end) {
        this.end = end;
    }
    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }
    /**
     * set the Capacity of this event
     *
     * @param capacity the max capacity of this event
     */
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    /**
     * Return the capacity of this event
     *
     * @return the capacity of this event
     */
    public int getCapacity() {
        return capacity;
    }
}