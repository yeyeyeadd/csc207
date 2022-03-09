package ca.utoronto.cs.dao;

import ca.utoronto.cs.entity.User;
import ca.utoronto.cs.exception.ObjectNotFoundException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;


public class UserDao implements IUserDao {
	private final Logger logger = LogManager.getLogger();
	private static final String FILENAME = "user.json";

	private final ObjectMapper mapper = new ObjectMapper();
	private List<User> all = new CopyOnWriteArrayList<>();


	@Override
	public void save(User obj) {
		if (obj.getId() > 0) {
			this.delete(obj);
		}
		else
			obj.setId(all.size() + 1);

		all.add(obj);

		saveToDisk();
	}

	private void load() {
		try {
			this.all = mapper.readValue(Paths.get(FILENAME).toFile(),
					new TypeReference<CopyOnWriteArrayList<User>>() {});
		} catch (IOException e) {
			logger.error("unable to load data");
			logger.error(e);
		}
	}

	@Override
	public User getById(int id) {
		this.load();
		Optional<User> model = all.stream().filter(i -> i.getId() == id).findFirst();
		if (!model.isPresent())
			throw new ObjectNotFoundException(String.valueOf(id));
		return model.get();
	}

	@Override
	public List<User> getAll() {
		this.load();
		return all.stream().map(i -> this.getById(i.getId())).collect(Collectors.toList());
	}

	@Override
	public void delete(User obj) {
		Optional<User> user = all.stream().filter(i -> i.getId() == obj.getId()).findFirst();
		user.ifPresent(value -> all.remove(value));
		saveToDisk();
	}

	private void saveToDisk(){
		try {
			mapper.writerWithDefaultPrettyPrinter()
					.writeValue(Paths.get(FILENAME).toFile(), all);
		} catch (IOException e) {
			logger.error("unable to save data");
			logger.error(e);
		}
	}

	private Optional<User> getQueryByName(String username) {
		this.load();
		return all.stream()
				.filter(i -> i.getUsername().equals(username))
				.findFirst()
				.map(i -> this.getById(i.getId()));
	}

	@Override
	public User getByName(String username) {
		Optional<User> user = this.getQueryByName(username);
		if (user.isPresent())
			return user.get();
		throw new ObjectNotFoundException(username);
	}

	@Override
	public boolean IsNameExist(String username) {
		return this.getQueryByName(username).isPresent();
	}

	@Override
	public long getUserCount() {
		this.load();
		return all.size();
	}
}
