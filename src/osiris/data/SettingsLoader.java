package osiris.data;

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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class SettingsLoader.
 * 
 * @author samuraiblood2
 * 
 */
public class SettingsLoader {

	/** The Constant FILE_EXTENSION. */
	private static final String FILE_EXTENSION = ".ini";

	/** The Constant SETTINGS_LOCATION. */
	private static final String SETTINGS_LOCATION = "./";

	/** The settings. */
	private static Hashtable<String, String> settings = new Hashtable<String, String>();

	/**
	 * Searches for all files ending with FILE_EXTENSION and parses them
	 * accordingly.
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void load() throws IOException {
		boolean found = false;
		File root = new File(SETTINGS_LOCATION);
		for (File child : root.listFiles()) {
			if (child == null) {
				continue;
			}

			if (!child.getName().endsWith(FILE_EXTENSION)) {
				continue;
			}

			BufferedReader in = new BufferedReader(new FileReader(child));
			List<String> lines = new ArrayList<String>();
			while (true) {
				String line = in.readLine();
				if (line == null) {
					break;
				}

				lines.add(line);
			}
			doParsing(lines.toArray(new String[0]));
			found = true;
		}

		if (!found) {
			createDefault();
			load();
		}
	}

	/**
	 * Creates the default.
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private static void createDefault() throws IOException {
		BufferedWriter out = new BufferedWriter(new FileWriter(new File(SETTINGS_LOCATION + "settings" + FILE_EXTENSION)));
		final String[] keys = { "server_port", "server_name", "exp_rate", "sqlite_saving", "server_host", "server_database", "server_username", "server_password", "start_x", "start_y", "start_z" };

		final String[] values = { "43594", "Osiris", "10", "true", "localhost", "database", "root", "admin", "3082", "3419", "0" };

		out.write("# Generated on " + new Date());
		out.newLine();
		out.newLine();
		for (int i = 0; i < keys.length; i++) {
			out.write(keys[i].toUpperCase() + ": " + values[i]);
			out.newLine();
		}
		out.flush();
		out.close();
	}

	/**
	 * Appends the keys and values to a Hashtable for later use.
	 * 
	 * @param lines
	 *            the lines
	 */
	private static void doParsing(String[] lines) {
		for (int i = 0; i < lines.length; i++) {
			String line = lines[i];
			int number = (i + 1);

			if (line == null) {
				continue;
			}

			if (line.matches("\\s*") || line.startsWith("#")) {
				continue;
			}

			String[] args = line.split("\\s*(=|:)\\s*");
			if (args.length < 2) {
				System.out.println("Missing argument at line " + number);
				continue;
			}

			String key = args[0];
			String value = args[1];
			settings.put(key.toLowerCase(), value);
		}
	}

	/**
	 * Gets the value.
	 * 
	 * @param key
	 *            the key
	 * @return the value
	 */
	public static String getValue(String key) {
		return settings.get(key.toLowerCase());
	}

	/**
	 * Gets the value as int.
	 * 
	 * @param key
	 *            the key
	 * @return the value as int
	 */
	public static int getValueAsInt(String key) {
		return Integer.parseInt(getValue(key).trim());
	}

	/**
	 * Gets the value as boolean.
	 * 
	 * @param key
	 *            the key
	 * @return the value as boolean
	 */
	public static boolean getValueAsBoolean(String key) {
		return Boolean.parseBoolean(settings.get(key.toLowerCase()));
	}

	/**
	 * Contains.
	 * 
	 * @param key
	 *            the key
	 * @return true, if successful
	 */
	public static boolean contains(String key) {
		return settings.containsKey(key.toLowerCase());
	}
}
