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

// TODO: Auto-generated Javadoc

/**
 * The block to make a player face to a certain coordinate?.
 * 
 * @author Blake
 * 
 */
public class FaceToCharacterBlock extends UpdateBlock {

	/**
	 * The face to.
	 */
	private final int faceTo;

	/**
	 * Instantiates a new face to block.
	 * 
	 * @param character
	 *            the character
	 * @param faceTo
	 *            the face to
	 */
	public FaceToCharacterBlock(Character character, int faceTo) {
		super(character, (character instanceof Npc ? 0x10 : 0x20), (character instanceof Npc ? 0 : 2));
		this.faceTo = faceTo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see osiris.game.update.UpdateBlock#append(osiris.io.PacketWriter)
	 */
	@Override
	public void append(PacketWriter writer) throws Exception {
		writer.writeShort(faceTo);
	}

}
