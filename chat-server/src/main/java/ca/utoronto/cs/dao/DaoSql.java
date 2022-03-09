package ca.utoronto.cs.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class DaoSql {
	protected final Connection conn;

	public DaoSql(Connection conn) {
		this.conn = conn;
	}
}
