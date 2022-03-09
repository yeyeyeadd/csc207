package ca.utoronto.cs.dao;

import ca.utoronto.cs.entity.Message;
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


public class MessageDao implements IMessageDao {
	private final Logger logger = LogManager.getLogger();
	private static final String FILENAME = "message.json";

	private final ObjectMapper mapper = new ObjectMapper();
	private List<Message> all = new CopyOnWriteArrayList<>();


	@Override
	public void save(Message obj) {
		if (obj.getId() > 0) {
			Optional<Message> user = all.stream().filter(i -> i.getId() == obj.getId()).findFirst();
			user.ifPresent(value -> all.remove(value));
		}
		else
			obj.setId(all.size() + 1);

		all.add(obj);

		saveToDisk();
	}

	private void load() {
		try {
			this.all = mapper.readValue(Paths.get(FILENAME).toFile(),
					new TypeReference<CopyOnWriteArrayList<Message>>() {});
		} catch (IOException e) {
			logger.error("unable to load data");
			logger.error(e);
		}
	}

	@Override
	public Message getById(int id) {
		this.load();
		Optional<Message> model = all.stream().filter(i -> i.getId() == id).findFirst();
		if (!model.isPresent())
			throw new ObjectNotFoundException(String.valueOf(id));
		return model.get();
	}

	@Override
	public List<Message> getAll() {
		this.load();
		return all.stream().map(i -> this.getById(i.getId())).collect(Collectors.toList());
	}

	@Override
	public void delete(Message obj) {
		Optional<Message> user = all.stream().filter(i -> i.getId() == obj.getId()).findFirst();
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

	@Override
	public List<Message> getByEventId(int eventId) {
		this.load();
		return all.stream().filter(i -> i.getEventId() == eventId).collect(Collectors.toList());
	}
}
