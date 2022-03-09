package ca.utoronto.cs.entity;

import java.util.Date;

public class Message {
	private int id;
	private int fromUserId;
	private int toUserId;
	private String from_alias;
	private Date time;
	private String content;
	private Integer eventId;

	/**
	 *
	 * @return the string version of the message
	 */
	@Override
	public String toString() {
		return "Message{" +
				"sender=" + getFromUserId() +
				", receiver=" + getToUserId() +
				", message='" + getContent() + '\'' +
				", time=" + getTime() +
				'}';
	}

	/**
	 *
	 * @return the message id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Set message id.
	 * @param id the id for this message.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 *
	 * @return the id for user who send message.
	 */
	public int getFromUserId() {
		return fromUserId;
	}

	/**
	 *
	 * @param fromUserId the id for sender.
	 */
	public void setFromUserId(int fromUserId) {
		this.fromUserId = fromUserId;
	}

	/**
	 * get user id send to.
	 * @return the id send to.
	 */
	public int getToUserId() {
		return toUserId;
	}

	/**
	 * set ToUserID
	 * @param toUserId the id for send to.
	 */
	public void setToUserId(int toUserId) {
		this.toUserId = toUserId;
	}

	/**
	 *getter for from alias
	 * @return a string form alias
	 */
	public String getFrom_alias() {
		return from_alias;
	}

	/**
	 * setter for from alias
	 * @param from_alias input from_alias
	 */
	public void setFrom_alias(String from_alias) {
		this.from_alias = from_alias;
	}

	/**
	 * getter for time
	 * @return the time of message send.
	 */
	public Date getTime() {
		return time;
	}

	/**
	 * setter for time.
	 * @param time the time of message send.
	 */
	public void setTime(Date time) {
		this.time = time;
	}

	/**
	 * getter for content
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * setter for content
	 * @param content the content of user send.
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * getter for eventid
	 * @return the eventid.
	 */
	public Integer getEventId() {
		return eventId;
	}

	/**
	 * setter for event id.
	 * @param eventId the eventid you send to.
	 */
	public void setEventId(int eventId) {
		this.eventId = eventId;
	}
}
