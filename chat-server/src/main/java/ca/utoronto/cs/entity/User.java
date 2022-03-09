package ca.utoronto.cs.entity;


import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class User {
	private int id;
	private String username;
	private String password;
	private Role role;
	private List<Integer> friends = new CopyOnWriteArrayList<>();

	/**
	 * set the password of this User
	 *
	 * @param password the password of this User
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * Return the password of this User
	 *
	 * @return the password of this User
	 */
	public String getPassword() {
		return this.password;
	}
	/**
	 * Return the id of this User
	 *
	 * @return the id of this User
	 */
	public int getId() {
		return this.id;
	}
	/**
	 * set the id of this User
	 *
	 * @param id the id of this User
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * Return True if User with same password
	 *
	 * @param password the password we want to compare
	 */
	public boolean check(String password) {
		return this.password.equals(password);
	}
	/**
	 * Return True if User with same id
	 *
	 * @param obj the User we want to compare
	 */
	@Override
	public boolean equals(Object obj) {
		// TODO: replace with user Id check?
		return this.getId() == ((User) obj).getId();
	}
	/**
	 * Return the id
	 *
	 * @return the id
	 */
	@Override
	public int hashCode() {
		return getId();
	}
	/**
	 * Return the username of this User
	 *
	 * @return the username of this User
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * set the username of this User
	 *
	 * @param username the username of this User
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * Return the role of this User
	 *
	 * @return the role of this User
	 */
	public Role getRole() {
		return role;
	}
	/**
	 * set the role of this User
	 *
	 * @param role the role of this User
	 */
	public void setRole(Role role) {
		this.role = role;
	}
	/**
	 * Return the friends of this User
	 *
	 * @return the friends of this User
	 */
	public List<Integer> getFriends() {
		return friends;
	}
	/**
	 * set the friends of this User
	 *
	 * @param friends the friends of this User
	 */
	public void setFriends(List<Integer> friends) {
		this.friends = friends;
	}
	/**
	 * Add the friend of this User
	 *
	 * @param id the id of this friend
	 */
	public void addFriendToFriends(int id){
		this.friends.add(id);
	}
	/**
	 *
	 * @return the string version of the User
	 */
	@Override
	public String toString() {
		return "User{" +
				"password='" + password + '\'' +
				", username='" + username + '\'' +
				", role=" + role +
				", friends=" + friends +
				'}';
	}
}
