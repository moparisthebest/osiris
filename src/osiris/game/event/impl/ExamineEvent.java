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
import osiris.game.model.def.ItemDef;
import osiris.io.ByteForm;
import osiris.io.Packet;
import osiris.io.PacketReader;
import osiris.io.ValueType;

// TODO: Auto-generated Javadoc
/**
 * Handles the examine button for items, NPC's, and objects.
 * 
 * @author samuraiblood2
 * 
 */
public class ExamineEvent extends GameEvent {

	/**
	 * Instantiates a new examine event.
	 * 
	 * @param player
	 *            the player
	 * @param packet
	 *            the packet
	 */
	public ExamineEvent(Player player, Packet packet) {
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
		switch (getPacket().getHeader().getOpcode()) {
		case 38:
			int id = reader.readShort(ValueType.A, ByteForm.LITTLE);
			getPlayer().getEventWriter().sendMessage(ItemDef.forId(id).getExamine());
			break;

		case 84:
			int objectId = reader.readShort(ValueType.A);
			getPlayer().getEventWriter().sendMessage("It's an object" + (getPlayer().getPlayerStatus() > 1 ? " id " + objectId + "" : "") + "!");
			break;

		case 88:
			reader.readShort();
			getPlayer().getEventWriter().sendMessage("It's an NPC!");
			break;

		}
	}

}
