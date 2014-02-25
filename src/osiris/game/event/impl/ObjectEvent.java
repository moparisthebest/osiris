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

import osiris.game.action.object.ObjectAction;
import osiris.game.action.object.ObjectAction.ObjectActionType;
import osiris.game.event.GameEvent;
import osiris.game.model.Player;
import osiris.game.model.Position;
import osiris.io.ByteForm;
import osiris.io.Packet;
import osiris.io.PacketReader;
import osiris.io.ValueType;

/**
 * The Class ObjectEvent.
 * 
 * @author Blake
 * 
 */
public class ObjectEvent extends GameEvent {

	/**
	 * Instantiates a new object event.
	 * 
	 * @param player
	 *            the player
	 * @param packet
	 *            the packet
	 */
	public ObjectEvent(Player player, Packet packet) {
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
		case 158:
			option1();
			break;
		case 228:
			option2();
			break;
		}
	}

	/**
	 * Option1.
	 */
	private void option1() {
		PacketReader reader = new PacketReader(getPacket());
		final int objectX = reader.readShort(false, ByteForm.LITTLE);
		final int objectID = reader.readShort(false);
		final int objectY = reader.readShort(false, ValueType.A, ByteForm.LITTLE);

		new ObjectAction(getPlayer(), new Position(objectX, objectY), 2, ObjectActionType.FIRST, objectID, objectX, objectY).run();
	}

	/**
	 * Option2.
	 */
	private void option2() {

	}

}
