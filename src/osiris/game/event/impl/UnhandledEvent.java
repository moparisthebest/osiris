package osiris.game.event.impl;

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
import osiris.game.event.GameEvent;
import osiris.game.model.Player;
import osiris.io.Packet;

// TODO: Auto-generated Javadoc

/**
 * The Class UnhandledEvent.
 * 
 * @author Blake
 */
public class UnhandledEvent extends GameEvent {

	/**
	 * Instantiates a new unhandled event.
	 * 
	 * @param player
	 *            the player
	 * @param packet
	 *            the packet
	 */
	public UnhandledEvent(Player player, Packet packet) {
		super(player, packet);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see osiris.game.event.GameEvent#process()
	 */
	@Override
	public void process() throws Exception {
		switch (getPacket().getHeader().getOpcode()) {
		case 63:
			getPlayer().getEventWriter().sendCloseChatboxInterface();
			break;

		default:
			if (Main.isLocal())
				System.out.println("Unhandled packet: " + getPacket());
			break;
		}
	}

}
