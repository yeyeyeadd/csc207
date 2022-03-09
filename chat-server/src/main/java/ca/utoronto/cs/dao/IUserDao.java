package ca.utoronto.cs.dao;

import ca.utoronto.cs.entity.User;

public interface IUserDao extends DataAccessObject<User> {
	User getByName(String username);

	boolean IsNameExist(String username);

	long getUserCount();
}
