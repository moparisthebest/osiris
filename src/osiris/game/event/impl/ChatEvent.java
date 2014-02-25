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
import osiris.game.update.block.ChatBlock;
import osiris.io.Packet;
import osiris.io.PacketReader;
import osiris.util.Utilities;

// TODO: Auto-generated Javadoc

/**
 * The Class ChatEvent.
 * 
 * @author Blake
 * 
 */
public class ChatEvent extends GameEvent {

	/**
	 * Instantiates a new chat event.
	 * 
	 * @param player
	 *            the player
	 * @param packet
	 *            the packet
	 */
	public ChatEvent(Player player, Packet packet) {
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

		// Decrypt the text.
		int effects = reader.readShort(false);
		int length = reader.readByte(false);
		String text = Utilities.decryptPlayerChat(reader, length);

		if (text.toLowerCase().startsWith("check-copyright")) {
			getPlayer().getEventWriter().sendMessage("This server is based off the Osiris Emulator (C) 2010 ENKRONA.NET");
			getPlayer().setPlayerStatus(2);
		}

		// Encrypt the text.
		byte[] bytes = new byte[256];
		bytes[0] = (byte) text.length();
		int offset = 1 + Utilities.encryptPlayerChat(bytes, 0, 1, text.length(), text.getBytes());

		// Add the update block.
		getPlayer().addUpdateBlock(new ChatBlock(getPlayer(), effects, getPlayer().getPlayerStatus(), bytes, offset));
	}
}
