package osiris.data.sql;

/*
 * Osiris Emulator
 * Copyright (C) 2011  Garrett Woodard, Blake Beaupain, Travis Burtrum
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 * 
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import osiris.Main;
import osiris.util.Settings;

// TODO: Auto-generated Javadoc
/**
 * The Class SqlConnection.
 * 
 * @author Boomer
 */
public class SqlConnection {

	/** The password. */
	private final String host, database, username, password;

	/** The connection. */
	private Connection connection;

	/**
	 * Instantiates a new sql connection.
	 * 
	 * @param host
	 *            the host
	 * @param database
	 *            the database
	 * @param username
	 *            the username
	 * @param password
	 *            the password
	 */
	public SqlConnection(final String host, final String database, final String username, final String password) {
		this.host = host;
		this.database = database;
		this.username = username;
		this.password = password;
	}

	/**
	 * Gets the connection.
	 * 
	 * @return the connection
	 */
	public Connection getConnection() {
		try {
			if (connection == null || connection.isClosed())
				connect();
		} catch (SQLException e) {
			connect();
		}
		return connection;
	}

	/**
	 * Connect.
	 */
	public void connect() {
		String tag = "[SQL" + (Settings.SQLITE_SAVING ? "ite" : "") + "]";
		try {
			if (Main.isLocal()) {
				System.out.println(tag + " Connecting to Database (" + database + ")... ");
			}

			if (!Settings.SQLITE_SAVING) {
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				String url = "jdbc:mysql://" + host + "/" + database;
				connection = DriverManager.getConnection(url, username, password);
			} else {
				Class.forName("org.sqlite.JDBC");
				connection = DriverManager.getConnection("jdbc:sqlite:./data/database/osirisdb.sqlite");

			}

			if (Main.isLocal()) {
				System.out.println(tag + " SUCCESS!");
			}
		} catch (Exception e) {
			System.err.println(tag + " FAILURE: " + e);
		}
	}

	/**
	 * Gets the host.
	 * 
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * Gets the database.
	 * 
	 * @return the database
	 */
	public String getDatabase() {
		return database;
	}

	/**
	 * Gets the username.
	 * 
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Gets the password.
	 * 
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
}
