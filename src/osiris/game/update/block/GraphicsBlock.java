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
import osiris.io.ByteForm;
import osiris.io.PacketWriter;
import osiris.io.ValueType;

/**
 * @author Blake
 * 
 */
public class GraphicsBlock extends UpdateBlock {

	/** The id. */
	private final int id;

	/** The height. */
	private final int height;

	/**
	 * Instantiates a new graphics block.
	 * 
	 * @param character
	 *            the character
	 * @param id
	 *            the id
	 * @param height
	 *            the height
	 */
	public GraphicsBlock(Character character, int id, int height) {
		super(character, (character instanceof Npc ? 0x2 : 0x400), (character instanceof Npc ? 4 : 4));
		this.id = id;
		if (height >= 100)
			height += 6553500;
		this.height = height;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see osiris.game.update.UpdateBlock#append(osiris.io.PacketWriter)
	 */

	@Override
	public void append(PacketWriter writer) throws Exception {
		if (getCharacter() instanceof Npc) {
			writer.writeShort(id, ValueType.A); // graphics id
			writer.writeInt(height, ByteForm.INVERSE_MIDDLE);
		} else {
			writer.writeShort(id);
			writer.writeInt(height, ByteForm.MIDDLE);
		}
	}

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public int getId() {
		return id;
	}

}
