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
import osiris.game.update.UpdateBlock;
import osiris.io.ByteForm;
import osiris.io.PacketWriter;

/**
 * @author Boomer
 * 
 */
public class TransformNpcBlock extends UpdateBlock {

	private int toNpcId;

	/**
	 * Instantiates a new update block.
	 * 
	 * @param character
	 *            the character
	 * @param toNpcId
	 *            the npc id transofrming into
	 */
	public TransformNpcBlock(Character character, int toNpcId) {
		super(character, 0x8, 1);
		this.toNpcId = toNpcId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see osiris.game.update.UpdateBlock#append(osiris.io.PacketWriter)
	 */
	@Override
	public void append(PacketWriter writer) throws Exception {
		writer.writeShort(toNpcId, ByteForm.LITTLE);
	}
}
