package osiris.game.event;

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

import osiris.Main;
import osiris.game.model.Player;
import osiris.io.Packet;

// TODO: Auto-generated Javadoc
/**
 * An abstract class for game events.
 * 
 * @author Blake
 * 
 */
public abstract class GameEvent {

	/**
	 * The player.
	 */
	private final Player player;

	/**
	 * The packet.
	 */
	private final Packet packet;

	/**
	 * Instantiates a new game event.
	 * 
	 * @param player
	 *            the player
	 * @param packet
	 *            the packet
	 */
	public GameEvent(Player player, Packet packet) {
		this.player = player;
		this.packet = packet;
	}

	/**
	 * Processes the game event.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	public abstract void process() throws Exception;

	/**
	 * Gets the player.
	 * 
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Gets the packet.
	 * 
	 * @return the packet
	 */
	public Packet getPacket() {
		return packet;
	}

}
