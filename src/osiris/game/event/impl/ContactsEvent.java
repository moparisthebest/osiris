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
import osiris.io.Packet;
import osiris.io.PacketReader;
import osiris.util.Utilities;

// TODO: Auto-generated Javadoc
/**
 * The Class ContactsEvent.
 * 
 * @author Boomer
 */
public class ContactsEvent extends GameEvent {

	/**
	 * Instantiates a new contacts event.
	 * 
	 * @param player
	 *            the player
	 * @param packet
	 *            the packet
	 */
	public ContactsEvent(Player player, Packet packet) {
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
		long name = reader.readLong();
		switch (getPacket().getHeader().getOpcode()) {
		case 30:
			getPlayer().getContacts().addFriend(name);
			break;

		case 61:
			getPlayer().getContacts().addIgnore(name);
			break;

		case 132:
			getPlayer().getContacts().removeFriend(name);
			break;

		case 2:
			getPlayer().getContacts().removeIgnore(name);
			break;

		case 178:
			int chars = reader.readByte();
			String text = Utilities.decryptPlayerChat(reader, chars);
			getPlayer().getContacts().sendMessage(name, text);
			break;
		}
	}
}
