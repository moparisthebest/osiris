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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import osiris.util.Settings;

// TODO: Auto-generated Javadoc
/**
 * The Class SqlManager.
 * 
 * @author Boomer
 */
public class SqlManager {

	/** The server connection. */
	public SqlConnection serverConnection;

	/** The player settings replace statement. */
	public PreparedStatement serverPlayerResultsStatement, serverPlayerReplaceStatement, playerSkillsResultsStatement, playerSkillsReplaceStatement, playerContactsResultsStatement, playerContactsReplaceStatement, playerItemsResultsStatement, playerItemsReplaceStatement, playerSettingsResultsStatement, playerSettingsReplaceStatement;

	/**
	 * Instantiates a new sql manager.
	 */
	public SqlManager() {
		serverConnection = new SqlConnection(Settings.SERVER_HOST, Settings.SERVER_DATABASE, Settings.SERVER_USERNAME, Settings.SERVER_PASSWORD);
		serverConnection.connect();

		String serverPlayerResultsQuery = "SELECT * FROM players WHERE name = ?";
		String playerSkillsResultsQuery = "SELECT * FROM skills WHERE player_id = ?";
		String playerContactsResultsQuery = "SELECT * FROM contacts WHERE player_id = ?";
		String playerItemsResultsQuery = "SELECT * FROM items WHERE player_id = ?";
		String playerSettingsResultsQuery = "SELECT * FROM settings WHERE player_id = ?";

		String serverPlayerReplaceQuery = "REPLACE INTO  players (`name` ,`password`, `ip`, `status`, `salt`) VALUES (?, ?, ?, ?, ?);";
		String playerSkillsReplaceQuery = "REPLACE INTO skills (`attack_curlevel`, `attack_maxlevel`, `attack_xp`, `defence_curlevel`, `defence_maxlevel`, `defence_xp`, `strength_curlevel`, `strength_maxlevel`, `strength_xp`, `hitpoints_curlevel`, `hitpoints_maxlevel`, `hitpoints_xp`, `range_curlevel`, `range_maxlevel`, `range_xp`, `prayer_curlevel`, `prayer_maxlevel`, `prayer_xp`, `magic_curlevel`, `magic_maxlevel`, `magic_xp`, `cooking_curlevel`, `cooking_maxlevel`, `cooking_xp`, `woodcutting_curlevel`, `woodcutting_maxlevel`, `woodcutting_xp`, `fletching_curlevel`, `fletching_maxlevel`, `fletching_xp`, `fishing_curlevel`, `fishing_maxlevel`, `fishing_xp`, `firemaking_curlevel`, `firemaking_maxlevel`, `firemaking_xp`, `crafting_curlevel`, `crafting_maxlevel`, `crafting_xp`, `smithing_curlevel`, `smithing_maxlevel`, `smithing_xp`, `mining_curlevel`, `mining_maxlevel`, `mining_xp`, `herblore_curlevel`, `herblore_maxlevel`, `herblore_xp`, `agility_curlevel`, `agility_maxlevel`, `agility_xp`, `thieving_curlevel`, `thieving_maxlevel`, `thieving_xp`, `slayer_curlevel`, `slayer_maxlevel`, `slayer_xp`, `farming_curlevel`, `farming_maxlevel`, `farming_xp`, `runecrafting_curlevel`, `runecrafting_maxlevel`, `runecrafting_xp`, `construction_curlevel`, `construction_maxlevel`, `construction_xp`, `hunter_curlevel`, `hunter_maxlevel`, `hunter_xp`, `summoning_curlevel`, `summoning_maxlevel`, `summoning_xp`, `overall_curlevel`, `overall_maxlevel`, `overall_xp`, `player_id`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		String playerContactsReplaceQuery = "REPLACE INTO contacts (`friends`, `ignores`, `player_id`) VALUES (?, ?, ?);";
		String playerItemsReplaceQuery = "REPLACE INTO items (`inventory_item_ids`, `inventory_item_amounts`, `bank_item_ids`, `bank_item_amounts`, `equipment_item_ids`, `equipment_item_amounts`, `player_id`) VALUES (?, ?, ?, ?, ?, ?, ?);";
		String playerSettingsReplaceQuery = "REPLACE INTO settings (`position_x`, `position_y`, `position_z`, `run_energy`, `special_energy`, `attack_style`, `spell_book`, `gender`, `auto_retaliate`, `emote_status`, `player_id`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

		try {
			serverPlayerResultsStatement = serverConnection.getConnection().prepareStatement(serverPlayerResultsQuery);
			serverPlayerReplaceStatement = serverConnection.getConnection().prepareStatement(serverPlayerReplaceQuery);

			playerSkillsReplaceStatement = serverConnection.getConnection().prepareStatement(playerSkillsReplaceQuery);
			playerSkillsResultsStatement = serverConnection.getConnection().prepareStatement(playerSkillsResultsQuery);

			playerContactsReplaceStatement = serverConnection.getConnection().prepareStatement(playerContactsReplaceQuery);
			playerContactsResultsStatement = serverConnection.getConnection().prepareStatement(playerContactsResultsQuery);

			playerItemsReplaceStatement = serverConnection.getConnection().prepareStatement(playerItemsReplaceQuery);
			playerItemsResultsStatement = serverConnection.getConnection().prepareStatement(playerItemsResultsQuery);

			playerSettingsReplaceStatement = serverConnection.getConnection().prepareStatement(playerSettingsReplaceQuery);
			playerSettingsResultsStatement = serverConnection.getConnection().prepareStatement(playerSettingsResultsQuery);

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Load.
	 */
	public static void load() {
		manager = new SqlManager();
	}

	/**
	 * Gets the results.
	 * 
	 * @param statement
	 *            the statement
	 * @param value
	 *            the value
	 * @return the results
	 */
	public ResultSet getResults(PreparedStatement statement, String value) {
		try {
			statement.setString(1, value);
			return statement.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Gets the all results.
	 * 
	 * @param table
	 *            the table
	 * @return the all results
	 */
	public ResultSet getAllResults(String table) {
		try {
			return serverConnection.getConnection().createStatement().executeQuery("SELECT * FROM " + table + "");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Gets the results.
	 * 
	 * @param query
	 *            the query
	 * @return the results
	 */
	public ResultSet getResults(String query) {
		try {
			return serverConnection.getConnection().createStatement().executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Gets the results.
	 * 
	 * @param statement
	 *            the statement
	 * @param value
	 *            the value
	 * @return the results
	 */
	public ResultSet getResults(PreparedStatement statement, int value) {
		try {
			statement.setInt(1, value);
			return statement.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/** The manager. */
	public static SqlManager manager;
}
