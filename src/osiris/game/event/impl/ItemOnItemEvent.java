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

import osiris.game.action.ItemActionListeners;
import osiris.game.action.item.ItemOnItemAction;
import osiris.game.event.GameEvent;
import osiris.game.model.Player;
import osiris.game.model.item.Item;
import osiris.io.ByteForm;
import osiris.io.Packet;
import osiris.io.PacketReader;
import osiris.io.ValueType;

/**
 * The Class ItemOnItemEvent.
 * 
 * @author Blake
 * 
 */
public class ItemOnItemEvent extends GameEvent {

	/**
	 * Instantiates a new item on item event.
	 * 
	 * @param player
	 *            the player
	 * @param packet
	 *            the packet
	 */
	public ItemOnItemEvent(Player player, Packet packet) {
		super(player, packet);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see osiris.game.event.GameEvent#process()
	 */

	@Override
	public void process() throws Exception {
		PacketReader reader = new PacketReader(getPacket());

		// skip unnecessary shit
		reader.readShort(ByteForm.LITTLE);
		reader.readShort(ValueType.A);
		reader.readInt();
		reader.readInt();

		int usedSlot = reader.readShort(ValueType.A);
		int usedOnSlot = reader.readShort(ValueType.A);
		Item used = getPlayer().getInventory().getItem(usedSlot);
		Item usedOn = getPlayer().getInventory().getItem(usedOnSlot);
		if (used == null || usedOn == null) {
			return;
		}

		// Fire the action.
		ItemActionListeners.fireItemAction(new ItemOnItemAction(getPlayer(), used, usedSlot, usedOn, usedOnSlot));
	}

}
