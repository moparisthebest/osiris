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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import osiris.Main;
import osiris.ServerEngine;
import osiris.data.sql.SqlManager;
import osiris.game.model.Player;
import osiris.game.model.Position;
import osiris.game.model.Skills;
import osiris.game.model.item.Item;
import osiris.util.Utilities;

// TODO: Auto-generated Javadoc
/**
 * The Class PlayerData.
 * 
 * @author Boomer
 */
public class PlayerData {

	/**
	 * Saves the player.
	 * 
	 * @param player
	 *            The player's instance.
	 * @return true on success, false on failure.
	 */
	public static boolean save(Player player) {
		int uniqueId = player.getUniqueId();

		// Skills
		try {
			saveSkills(SqlManager.manager.playerSkillsReplaceStatement, player, uniqueId);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

		// Contacts
		try {
			saveContacts(SqlManager.manager.playerContactsReplaceStatement, player, uniqueId);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

		// Items
		try {
			saveItems(SqlManager.manager.playerItemsReplaceStatement, player, uniqueId);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

		// Settings
		try {
			saveSettings(SqlManager.manager.playerSettingsReplaceStatement, player, uniqueId);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/*
	 * Return Codes: 1 = wait 2 seconds 2 = success 3 = wrong pass 4 = disabled
	 * 5 = logged in already 6 = reload client 7 = full 8 = unable to connect 9
	 * = too many connections from ip 10= bad session id 11 = please try again
	 * 12 = members 13 = could not complete login 14 = server is being updated
	 * 16 = login attemps exceeded 17 = standing in members area 20 = invalid
	 * login request 21 = just left another world
	 */

	/**
	 * Loads the player.
	 * 
	 * @param playerName
	 *            The player's name.
	 * @param pass
	 *            The player's password.
	 * @param player
	 *            The player's instance.
	 * @return A return code to be sent to the client.
	 */
	public static int load(String playerName, String pass, Player player) {
		// Is the server down for an update?
		if (ServerEngine.isUnderLockdown()) {
			return 14;
		}

		// Is the player already logged in?
		for (Player p : Main.getPlayers()) {
			if (p.getUsername().equalsIgnoreCase(playerName))
				return 5;
		}

		if (SqlManager.manager != null) {
			try {
				ResultSet playerResults = SqlManager.manager.getResults(SqlManager.manager.serverPlayerResultsStatement, playerName);

				boolean foundUser = false;
				while (playerResults.next()) {
					player.setUniqueId(playerResults.getInt("id"));

					if (player.getUniqueId() == -1)
						return 3;

					int salt = playerResults.getInt("salt");
					// Is the player's password correct?
					if (!playerResults.getString("password").equals(Utilities.smfHash(pass, salt)))
						return 3;

					// Retrieve the player's status.
					player.setPlayerStatus(playerResults.getInt("status"));
					foundUser = true;
				}

				// Player doesn't exist.
				if (!foundUser || player.getUniqueId() == -1)
					return 3;

				// Retrieve items for the player.
				ResultSet itemsResults = SqlManager.manager.getResults(SqlManager.manager.playerItemsResultsStatement, player.getUniqueId());
				if (itemsResults != null) {
					while (itemsResults.next()) {
						String inventoryIds = itemsResults.getString("inventory_item_ids").replaceAll("\\[", "").replaceAll("\\]", "");
						String inventoryAmounts = itemsResults.getString("inventory_item_amounts").replaceAll("\\[", "").replaceAll("\\]", "");

						String[] ids = inventoryIds.split(", ");
						String[] amounts = inventoryAmounts.split(", ");
						Item[] items = new Item[ids.length];

						for (int i = 0; i < ids.length; i++) {
							int id = Integer.parseInt(ids[i]);

							if (id == -1) {
								items[i] = null;
							} else {
								items[i] = Item.create(id, Integer.parseInt(amounts[i]));
							}
						}

						player.getInventory().setItems(items);

						String bankIds = itemsResults.getString("bank_item_ids").replaceAll("\\[", "").replaceAll("\\]", "");
						String bankAmounts = itemsResults.getString("bank_item_amounts").replaceAll("\\[", "").replaceAll("\\]", "");

						ids = bankIds.split(", ");
						amounts = bankAmounts.split(", ");
						items = new Item[ids.length];

						for (int i = 0; i < ids.length; i++) {
							int id = Integer.parseInt(ids[i]);

							if (id == -1) {
								items[i] = null;
							} else {
								items[i] = Item.create(id, Integer.parseInt(amounts[i]));
							}
						}

						player.getBank().getMain().setItems(items);

						String equipmentIds = itemsResults.getString("equipment_item_ids").replaceAll("\\[", "").replaceAll("\\]", "");
						String equipmentAmounts = itemsResults.getString("equipment_item_amounts").replaceAll("\\[", "").replaceAll("\\]", "");

						ids = equipmentIds.split(", ");
						amounts = equipmentAmounts.split(", ");
						items = new Item[ids.length];

						for (int i = 0; i < ids.length; i++) {
							int id = Integer.parseInt(ids[i]);

							if (id == -1) {
								items[i] = null;
							} else {
								items[i] = Item.create(id, Integer.parseInt(amounts[i]));
							}
						}

						player.getEquipment().setItems(items);
					}
				}

				// Retrieve the contacts for the player.
				ResultSet contactsResults = SqlManager.manager.getResults(SqlManager.manager.playerContactsResultsStatement, player.getUniqueId());
				if (contactsResults != null) {
					while (contactsResults.next()) {
						String friendIds = contactsResults.getString("friends").replaceAll("\\[", "").replaceAll("\\]", "");
						String[] friends = friendIds.split(", ");

						for (String friend : friends) {
							if (friend.length() != 0) {
								player.getContacts().getFriends().add(Long.parseLong(friend));
							}
						}

						String ignoreIds = contactsResults.getString("ignores").replaceAll("\\[", "").replaceAll("\\]", "");
						String[] ignores = ignoreIds.split(", ");

						for (String ignore : ignores) {
							if (ignore.length() != 0) {
								player.getContacts().getIgnores().add(Long.parseLong(ignore));
							}
						}
					}
				}

				// Retrieve the settings for the player.
				ResultSet settingsResults = SqlManager.manager.getResults(SqlManager.manager.playerSettingsResultsStatement, player.getUniqueId());
				if (settingsResults != null) {
					while (settingsResults.next()) {
						player.setLoadedPosition(new Position(settingsResults.getInt("position_x"), settingsResults.getInt("position_y"), settingsResults.getInt("position_z")));
						player.setEnergy(settingsResults.getInt("run_energy"), false);
						player.setSpecialEnergy(settingsResults.getInt("special_energy"), false);
						player.setAttackStyle(settingsResults.getInt("attack_style"), false);
						String spellBook = settingsResults.getString("spell_book");
						player.setSpellBook(spellBook);
						player.getAppearance().setGender(settingsResults.getInt("gender"));
						player.getSettings().setAutoRetaliate(settingsResults.getInt("auto_retaliate") == 1);
						String[] emoteStatuses = settingsResults.getString("emote_status").replaceAll("\\[", "").replaceAll("\\]", "").split(", ");
						for (int i = 0; i < emoteStatuses.length; i++)
							player.getEmoteStatus()[i] = Integer.parseInt(emoteStatuses[i]);

					}
				}

				// Retrieve the skills for the player.
				ResultSet skillsResults = SqlManager.manager.getResults(SqlManager.manager.playerSkillsResultsStatement, player.getUniqueId());
				if (skillsResults != null) {
					while (skillsResults.next()) {
						int columnIndex = 2;

						for (int i = 0; i < Skills.NUMBER_OF_SKILLS; i++) {
							int curLevel = skillsResults.getInt(columnIndex++);
							columnIndex++; // Omitting maxLevel
							double xp = skillsResults.getDouble(columnIndex++);

							player.getSkills().setSkill(i, curLevel, xp, false);
						}
					}
				}
				return 2;
			}

			catch (Exception e) {
				e.printStackTrace();
				return 3;
			}

		}
		if (player.getUniqueId() == -1)
			return 3;

		return 13;
	}

	/**
	 * Saves the player's skills.
	 * 
	 * @param statement
	 *            The prepared statement for saving.
	 * @param player
	 *            The player's instance.
	 * @param uniqueId
	 *            The player's ID.
	 * @throws SQLException
	 *             On a MySQL failure.
	 */
	public static void saveSkills(PreparedStatement statement, Player player, int uniqueId) throws SQLException {
		int[] curLevels = player.getSkills().getCurLevels();
		int[] maxLevels = player.getSkills().getMaxLevels();
		double[] experience = player.getSkills().getExperience();

		int totalCurLevels = 0;
		int totalMaxLevels = 0;
		double totalExp = 0.0;

		int statementIndex = 0;

		// Iterate through the skills.
		for (int i = 0; i < Skills.NUMBER_OF_SKILLS; i++) {
			statement.setInt(++statementIndex, curLevels[i]);
			statement.setInt(++statementIndex, maxLevels[i]);
			statement.setDouble(++statementIndex, experience[i]);

			totalCurLevels += curLevels[i];
			totalMaxLevels += maxLevels[i];
			totalExp += experience[i];
		}

		statement.setInt(++statementIndex, totalCurLevels);
		statement.setInt(++statementIndex, totalMaxLevels);
		statement.setDouble(++statementIndex, totalExp);

		statement.setInt(++statementIndex, uniqueId);
		statement.executeUpdate();
	}

	/**
	 * Saves the player's contacts.
	 * 
	 * @param statement
	 *            The prepared statement for saving.
	 * @param player
	 *            The player's instance.
	 * @param uniqueId
	 *            The player's ID.
	 * @throws SQLException
	 *             On a MySQL failure.
	 */
	public static void saveContacts(PreparedStatement statement, Player player, int uniqueId) throws SQLException {
		statement.setString(1, Arrays.toString(player.getContacts().getFriends().toArray(new Long[player.getContacts().getFriends().size()])));
		statement.setString(2, Arrays.toString(player.getContacts().getIgnores().toArray(new Long[player.getContacts().getIgnores().size()])));
		statement.setInt(3, uniqueId);
		statement.executeUpdate();
	}

	/**
	 * Saves the player's items.
	 * 
	 * @param statement
	 *            The prepared statement for saving.
	 * @param player
	 *            The player's instance.
	 * @param uniqueId
	 *            The player's ID.
	 * @throws SQLException
	 *             On a MySQL failure.
	 */
	public static void saveItems(PreparedStatement statement, Player player, int uniqueId) throws SQLException {
		Item[][] containers = new Item[][] { player.getInventory().getItems(), player.getBank().getMain().getItems(), player.getEquipment().getItems() };
		int saveSlot = 1;

		for (Item[] container : containers) {
			int[] itemIds = new int[container.length];
			int[] itemNs = new int[container.length];

			for (int i = 0; i < container.length; i++) {
				Item item = container[i];

				if (item == null) {
					itemIds[i] = -1;
					itemNs[i] = 0;
				} else {
					itemIds[i] = item.getId();
					itemNs[i] = item.getAmount();
				}
			}

			statement.setString(saveSlot, Arrays.toString(itemIds));
			saveSlot++;

			statement.setString(saveSlot, Arrays.toString(itemNs));
			saveSlot++;
		}

		statement.setInt(saveSlot, uniqueId);
		statement.executeUpdate();
	}

	/**
	 * Saves the player's settings.
	 * 
	 * @param statement
	 *            The prepared statement for saving.
	 * @param player
	 *            The player's instance.
	 * @param uniqueId
	 *            The player's ID.
	 * @throws SQLException
	 *             On a MySQL failure.
	 */
	public static void saveSettings(PreparedStatement statement, Player player, int uniqueId) throws SQLException {
		statement.setInt(1, player.getPosition().getX());
		statement.setInt(2, player.getPosition().getY());
		statement.setInt(3, player.getPosition().getZ());
		statement.setInt(4, player.getEnergy());
		statement.setInt(5, player.getSpecialEnergy());
		statement.setInt(6, player.getAttackStyle());
		statement.setString(7, player.getSpellBook().name());
		statement.setInt(8, player.getAppearance().getGender());
		statement.setInt(9, player.getSettings().isAutoRetaliate() ? 1 : 0);
		statement.setString(10, Arrays.toString(player.getEmoteStatus()));
		statement.setInt(11, uniqueId);
		statement.executeUpdate();
	}

}
