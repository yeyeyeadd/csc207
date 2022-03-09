package ca.utoronto.cs.dao;

import ca.utoronto.cs.exception.ObjectNotFoundException;
import ca.utoronto.cs.entity.Room;
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

public class RoomDao implements IRoomDao {
	private final Logger logger = LogManager.getLogger();
	private static final String FILENAME = "room.json";

	private final ObjectMapper mapper = new ObjectMapper();
	private List<Room> all = new CopyOnWriteArrayList<>();

	@Override
	public void save(Room obj) {
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
					new TypeReference<CopyOnWriteArrayList<Room>>() {});
		} catch (IOException e) {
			logger.error("unable to load data");
			logger.error(e);
		}
	}

	@Override
	public Room getById(int id) {
		this.load();
		Optional<Room> model = all.stream().filter(i -> i.getId() == id).findFirst();
		if (!model.isPresent())
			throw new ObjectNotFoundException(String.valueOf(id));
		return model.get();
	}

	@Override
	public List<Room> getAll() {
		this.load();
		return all.stream().map(i -> this.getById(i.getId())).collect(Collectors.toList());
	}

	@Override
	public void delete(Room obj) {
		Optional<Room> room = all.stream().filter(i -> i.getId() == obj.getId()).findFirst();
		room.ifPresent(value -> all.remove(value));
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

	private Optional<Room> getQueryByName(String name) {
		this.load();
		return all.stream()
				.filter(i -> i.getName().equals(name))
				.findFirst()
				.map(i -> this.getById(i.getId()));
	}

	@Override
	public Room getByName(String name) {
		Optional<Room> user = this.getQueryByName(name);
		if (user.isPresent())
			return user.get();
		throw new ObjectNotFoundException(name);
	}

	@Override
	public boolean IsNameExist(String name) {
		return this.getQueryByName(name).isPresent();
	}
}
