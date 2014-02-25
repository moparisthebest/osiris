package osiris.game.model;

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

import java.util.ArrayList;
import java.util.List;

import osiris.Main;
import osiris.util.Settings;
import osiris.util.Utilities;

// TODO: Auto-generated Javadoc
/**
 * The Class Contacts.
 * 
 * @author samuraiblood2
 * @author Boomer
 * 
 */
public class Contacts {

	/** The messages. */
	private int messages = 1;

	/** The friends. */
	private List<Long> friends = new ArrayList<Long>();

	/** The ignores. */
	private List<Long> ignores = new ArrayList<Long>();

	/** The player. */
	private final Player player;

	/**
	 * Instantiates a new friends.
	 * 
	 * @param player
	 *            the player
	 */
	public Contacts(Player player) {
		this.player = player;
	}

	/**
	 * Adds the friend.
	 * 
	 * @param id
	 *            the id
	 */
	public void addFriend(long id) {
		if (id == player.getUsernameAsLong()) {
			return;
		}
		if (friends.size() >= Settings.FRIENDS_LIMIT) {
			return;
		}

		if (friends.contains(id)) {
			return;
		}

		friends.add(id);
		player.getEventWriter().sendFriend(id, getWorld(id));
	}

	/**
	 * Removes the friend.
	 * 
	 * @param id
	 *            the id
	 */
	public void removeFriend(long id) {
		friends.remove(id);
	}

	/**
	 * Adds the ignore.
	 * 
	 * @param id
	 *            the id
	 */
	public void addIgnore(long id) {
		if (ignores.size() >= Settings.IGNORE_LIMIT) {
			return;
		}

		if (ignores.contains(id)) {
			return;
		}

		ignores.add(id);
	}

	/**
	 * Removes the ignore.
	 * 
	 * @param id
	 *            the id
	 */
	public void removeIgnore(long id) {
		ignores.remove(id);
	}

	/**
	 * Refresh.
	 */
	public void refresh() {
		player.getEventWriter().sendFriendsStatus(2);
		for (Long friend : friends) {
			player.getEventWriter().sendFriend(friend, getWorld(friend));
		}
		player.getEventWriter().sendIgnores((ignores.toArray(new Long[ignores.size()])));
	}

	/**
	 * Send message.
	 * 
	 * @param name
	 *            the name
	 * @param message
	 *            the message
	 */
	public void sendMessage(long name, String message) {
		for (Player friend : Main.getPlayers()) {
			if (friend == null)
				continue;

			if (friend.getUsernameAsLong() == name) {
				friend.getEventWriter().sendReceivedPrivateMessage(player.getUsernameAsLong(), player.getPlayerStatus(), message);
				player.getEventWriter().sendSentPrivateMessage(name, message);
				return;
			}
		}
		player.getEventWriter().sendMessage(Utilities.longToString(name) + " is currently offline.");
	}

	/**
	 * Sets the online status.
	 * 
	 * @param online
	 *            the new online status
	 */
	public void setOnlineStatus(boolean online) {
		for (Player plr : Main.getPlayers()) {
			if (plr == null)
				continue;
			setOnlineStatus(plr, online);
		}
	}

	/**
	 * Sets the online status.
	 * 
	 * @param friend
	 *            the friend
	 * @param online
	 *            the online
	 */
	private void setOnlineStatus(Player friend, boolean online) {
		if (friend.getContacts().friends.contains(player.getUsernameAsLong()))
			friend.getEventWriter().sendFriend(player.getUsernameAsLong(), online ? 1 : 0);
	}

	/**
	 * Gets the world.
	 * 
	 * @param id
	 *            the id
	 * @return the world
	 */
	private int getWorld(Long id) {
		for (Player friend : Main.getPlayers()) {
			if (friend == null)
				continue;
			else if (friend.getUsernameAsLong() == id)
				return 1;
		}
		return 0;
	}

	/**
	 * Gets the messages.
	 * 
	 * @return the messages
	 */
	public int getMessages() {
		return messages++;
	}

	/**
	 * Gets the friends.
	 * 
	 * @return the friends
	 */
	public List<Long> getFriends() {
		return friends;
	}

	/**
	 * Gets the ignores.
	 * 
	 * @return the ignores
	 */
	public List<Long> getIgnores() {
		return ignores;
	}
}
