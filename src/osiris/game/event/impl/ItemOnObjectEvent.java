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

import osiris.game.action.object.ItemOnObjectAction;
import osiris.game.action.object.ObjectAction;
import osiris.game.event.GameEvent;
import osiris.game.model.Player;
import osiris.game.model.Position;
import osiris.io.ByteForm;
import osiris.io.Packet;
import osiris.io.PacketReader;
import osiris.io.ValueType;
import osiris.util.Utilities;

// TODO: Auto-generated Javadoc
/**
 * The Class ItemOnObjectEvent.
 * 
 * @author Boomer
 */
public class ItemOnObjectEvent extends GameEvent {
	/**
	 * Instantiates a new game event.
	 * 
	 * @param player
	 *            the player
	 * @param packet
	 *            the packet
	 */
	public ItemOnObjectEvent(Player player, Packet packet) {
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
		int y = reader.readShort(ByteForm.LITTLE);
		int itemId = reader.readShort();
		reader.readInt();
		int itemSlot = reader.readShort(ValueType.A);
		int objectId = reader.readShort(ValueType.A);
		int x = reader.readShort();
		Position pos = new Position(x, y, getPlayer().getPosition().getZ());
		int distance = Utilities.getDistance(pos, getPlayer().getPosition());
		new ItemOnObjectAction(getPlayer(), pos, distance, ObjectAction.ObjectActionType.FIRST, objectId, x, y, itemSlot, itemId).run();
	}
}
