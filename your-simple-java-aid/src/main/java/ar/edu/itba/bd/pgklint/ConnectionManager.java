package ar.edu.itba.bd.pgklint;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
	private boolean inUse = false;
	private Connection dbConnection;

	public ConnectionManager(String host, Integer port, String database, String user, String password) {
		super();
		try {
			Class.forName("org.postgresql.Driver ");
		} catch (Exception e) {

		}
		try {
			this.dbConnection = DriverManager.getConnection("jdbc:postgresql://"+host+":"+port+"/"+database, user,
					password);
		} catch (SQLException e) {

		}
	}

	public ConnectionManager release() {
		if (!this.inUse) {
			throw new IllegalStateException("Cannot release connection that is not in use.");
		}
		this.inUse = false;
		return this;
	}

	public void close() throws SQLException {
		this.dbConnection.close();
	}

	public Connection hold() {
		if (this.inUse) {
			throw new IllegalStateException("Cannot hold connection that is in use.");
		}
		this.inUse = true;
		return this.dbConnection;
	}
}
