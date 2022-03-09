package ca.utoronto.cs.service;

import ca.utoronto.cs.entity.User;
import io.netty.channel.Channel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;


public class ConnectionManager {
	private final Logger logger = LogManager.getLogger();

	private final Map<User, List<Channel>> _forward = new ConcurrentHashMap<>();
	private final Map<Channel, User> _backward = new ConcurrentHashMap<>();


	/**
	 * Add a user connect with server
	 * @param user giving user
	 * @param channel input Channel
	 */
	public void addConnection(User user, Channel channel) {
		if (!_forward.containsKey(user))
			_forward.put(user, new CopyOnWriteArrayList<Channel>());
		List<Channel> list = _forward.get(user);
		list.add(channel);

		if (_backward.containsKey(channel))
			logger.warn("Connection existed in backward lookup table");
		_backward.put(channel, user);
	}

	/**
	 * remove a connection with a channel
	 * @param channel input Channel
	 */
	public void removeConnection(Channel channel) {
		User user = _backward.remove(channel);
		if (user == null) {
			logger.warn("Connection does not exist in backward lookup table." +
					" I will run a full scan to check forward lookup table.");
			for (User u: _forward.keySet()) {
				for (Channel item : _forward.get(u)) {
					if (item == channel) {
						user = u;
						break;
					}
				}
			}
		}
		if (user == null) {
			logger.warn("Connection does not exist in forward lookup table.");
			return;
		}

		List<Channel> list = _forward.get(user);
		if (list.size() <= 1)
			_forward.remove(user);
		else
			list.remove(channel);
	}

	/**
	 * return list of channel for user
	 * @param user giving user
	 * @return list of channel for user
	 */
	public List<Channel> getChannelsByUser(User user) {
		return _forward.getOrDefault(user, new CopyOnWriteArrayList<>());
	}

	/**
	 * return user for that channel
	 * @param channel giving channel
	 * @return  user for that channel
	 */
	public User getUserByChannel(Channel channel) {
		return _backward.get(channel);
	}

	/**
	 * modify channel with giving user
	 * @param channel giving channel
	 * @param user giving user.
	 */
	public void switchUser(Channel channel, User user) {
		removeConnection(channel);
		addConnection(user, channel);
	}
}