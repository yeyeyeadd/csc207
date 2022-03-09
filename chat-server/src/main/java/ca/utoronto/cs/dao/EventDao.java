package ca.utoronto.cs.dao;

import ca.utoronto.cs.exception.ObjectNotFoundException;
import ca.utoronto.cs.entity.Event;
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

public class EventDao implements IEventDao {
	private final Logger logger = LogManager.getLogger();
	private static final String FILENAME = "event.json";

	private final ObjectMapper mapper = new ObjectMapper();
	private List<Event> all = new CopyOnWriteArrayList<>();

	@Override
	public void save(Event obj) {
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
					new TypeReference<CopyOnWriteArrayList<Event>>() {});
		} catch (IOException e) {
			logger.error("unable to load data");
			logger.error(e);
		}
	}

	@Override
	public Event getById(int id) {
		this.load();
		Optional<Event> model = all.stream().filter(i -> i.getId() == id).findFirst();
		if (!model.isPresent())
			throw new ObjectNotFoundException(String.valueOf(id));
		return model.get();
	}

	@Override
	public List<Event> getAll() {
		this.load();
		return all.stream().map(i -> this.getById(i.getId())).collect(Collectors.toList());
	}

	@Override
	public void delete(Event obj) {
		this.load();
		Optional<Event> room = all.stream().filter(i -> i.getId() == obj.getId()).findFirst();
		room.ifPresent(value -> all.remove(value));
		saveToDisk();
	}

	private void saveToDisk() {
		try {
			mapper.writerWithDefaultPrettyPrinter()
					.writeValue(Paths.get(FILENAME).toFile(), all);
		} catch (IOException e) {
			logger.error("unable to save data");
			logger.error(e);
		}
	}

	private Optional<Event> getQueryByName(String name) {
		this.load();
		return all.stream()
				.filter(i -> i.getName().equals(name))
				.findFirst()
				.map(i -> this.getById(i.getId()));
	}

	@Override
	public Event getByName(String name) {
		Optional<Event> event = this.getQueryByName(name);
		if (event.isPresent())
			return event.get();
		throw new ObjectNotFoundException(name);
	}

	@Override
	public boolean IsNameExist(String name) {
		return this.getQueryByName(name).isPresent();
	}
}
