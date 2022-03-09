package ca.utoronto.cs.dao;

import java.util.List;

public interface DataAccessObject<T> {
	void save(T obj);
	T getById(int id);
	List<T> getAll();
	void delete(T obj);
}
