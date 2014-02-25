package osiris.game.update.block;

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

import osiris.game.model.Character;
import osiris.game.model.Npc;
import osiris.game.update.UpdateBlock;
import osiris.io.PacketWriter;

/**
 * @author Boomer
 * 
 */
public class ForcedChatBlock extends UpdateBlock {

	/** The chat. */
	private String chat;

	/**
	 * Instantiates a new update block.
	 * 
	 * @param character
	 *            the character
	 * @param chat
	 *            the chat
	 */
	public ForcedChatBlock(Character character, String chat) {
		super(character, character instanceof Npc ? 0x40 : 0x4, character instanceof Npc ? 2 : 3);
		this.chat = chat;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see osiris.game.update.UpdateBlock#append(osiris.io.PacketWriter)
	 */
	@Override
	public void append(PacketWriter writer) throws Exception {
		writer.writeString(chat);
	}
}
