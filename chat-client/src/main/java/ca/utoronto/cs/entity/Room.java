package ca.utoronto.cs.entity;


public class Room {
	private int id;
	private String name;
	private int capacity;
	/**
	 * Return the name of this room
	 *
	 * @return the name of this room
	 */
	public String getName() {
		return this.name;
	}
	/**
	 * set the name of this room
	 *
	 * @param name the name of this room
	 */
	public void setName(String name) { this.name = name; }
	/**
	 * Return True if room with same id
	 *
	 * @param obj the obj we want to compare
	 */
	@Override
	public boolean equals(Object obj) {
		return this.id == ((Room) obj).id;
	}
	/**
	 * Return the id of this room
	 *
	 * @return the id of this room
	 */
	public int getId() {
		return id;
	}
	/**
	 * set the id of this room
	 *
	 * @param id the id of this room
	 */
	public void setId(int id) {
		this.id = id;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
}
