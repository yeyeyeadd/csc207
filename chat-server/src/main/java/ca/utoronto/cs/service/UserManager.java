package ca.utoronto.cs.service;

import ca.utoronto.cs.SystemUser;
import ca.utoronto.cs.dao.IUserDao;
import ca.utoronto.cs.dao.UserDao;
import ca.utoronto.cs.entity.Role;
import ca.utoronto.cs.exception.UserAlreadyExistedException;
import ca.utoronto.cs.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserManager {
	private final Logger logger = LogManager.getLogger();
	private IUserDao userDao;

	public void setUserDao(IUserDao userDao) {
		this.userDao = userDao;

		if (userDao.getUserCount() <= 0) {
			logger.debug("creating default users");
			this.createUser("ANONYMOUS", "Qqwkdh0921e@E---", Role.ANONYMOUS);
			this.createUser("admin", "admin", Role.OPERATOR);
			this.createUser("demo", "demo", Role.USER);
		}
	}

	/**
	 * return a user by giving username
	 * @param username the username in server
	 * @return return a user by giving username
	 */
	public User getUserByName(String username) {
		logger.debug("getting user username={}", username);

		return userDao.getByName(username);
	}

	/**
	 * create a user by giving name, password and role
	 * @param username username
	 * @param password password
	 * @param role role
	 * @return a user.
	 */
	public User createUser(String username, String password, Role role) {
		logger.debug("creating user, username={}, password={}, role={}", username, password, role);
		if (userDao.IsNameExist(username))
			throw new UserAlreadyExistedException(username);
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		user.setRole(role);
		userDao.save(user);
		return user;
	}

	/**
	 * get current user for system user.
	 * @param user a systemUser user
	 * @return a suer.
	 */
	public User getSystemUser(SystemUser user) {
		logger.debug("getting system user: {}", user);
		return userDao.getByName(user.name());
	}

	/**
	 * get user for giving user id.
	 * @param id giving id
	 * @return a use
	 */
	public User getUserById(int id) {
		return userDao.getById(id);
	}

	/**
	 * save user to server.
	 * @param user user
	 */
	public void save(User user) {
		userDao.save(user);
	}
}