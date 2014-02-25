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

import osiris.game.event.GameEvent;
import osiris.game.model.Player;
import osiris.game.model.WorldObjects;
import osiris.game.model.ground.GroundManager;
import osiris.io.Packet;

// TODO: Auto-generated Javadoc
/**
 * The Class RegionLoadedEvent.
 * 
 * @author Boomer
 */
public class RegionLoadedEvent extends GameEvent {

	/**
	 * Instantiates a new region loaded event.
	 * 
	 * @param player
	 *            the player
	 * @param packet
	 *            the packet
	 */
	public RegionLoadedEvent(Player player, Packet packet) {
		super(player, packet);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see osiris.game.event.GameEvent#process()
	 */
	@Override
	public void process() throws Exception {
		GroundManager.getManager().refreshLandscapeDisplay(getPlayer());
		WorldObjects.refresh(getPlayer());
	}

}
