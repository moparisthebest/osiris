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
import osiris.game.model.Position;
import osiris.game.update.UpdateBlock;
import osiris.io.ByteForm;
import osiris.io.PacketWriter;
import osiris.io.ValueType;

/**
 * @author Boomer
 * 
 */
public class FaceToPositionBlock extends UpdateBlock {

	/** The position. */
	private Position position;

	/**
	 * Instantiates a new update block.
	 * 
	 * @param character
	 *            the character
	 * @param position
	 *            the position
	 */
	public FaceToPositionBlock(Character character, Position position) {
		super(character, (character instanceof Npc ? 0x80 : 0x40), (character instanceof Npc ? 6 : 5));
		this.position = position;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see osiris.game.update.UpdateBlock#append(osiris.io.PacketWriter)
	 */
	@Override
	public void append(PacketWriter writer) throws Exception {
		if (getCharacter() instanceof Npc) {
			writer.writeShort((position.getX() * 2) + 1, ValueType.A);
			writer.writeShort((position.getY() * 2) + 1, ValueType.A, ByteForm.LITTLE);
		} else {
			writer.writeShort((position.getX() * 2) + 1, ByteForm.LITTLE);
			writer.writeShort((position.getY() * 2) + 1, ValueType.A);
		}
	}

}