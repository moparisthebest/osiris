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
import osiris.io.ValueType;

/**
 * The secondary damage hit block.
 * 
 * @author Blake
 * 
 */
public class SecondaryHitBlock extends UpdateBlock {

	/** The amount. */
	private final int damage;

	/** The type. */
	private final int type;

	/**
	 * Instantiates a new secondary hit block.
	 * 
	 * @param character
	 *            the character
	 * @param damage
	 *            the damage
	 * @param type
	 *            the type
	 */
	public SecondaryHitBlock(Character character, int damage, int type) {
		super(character, (character instanceof Npc ? 0x20 : 0x200), (character instanceof Npc ? 5 : 1));
		this.type = type;
		this.damage = damage;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see osiris.update.UpdateBlock#append(osiris.io.PacketWriter)
	 */

	@Override
	public void append(PacketWriter writer) throws Exception {
		if (getCharacter() instanceof Npc) {
			writer.writeByte(damage); // damage amount
			writer.writeByte(type, ValueType.S);
		} else {
			writer.writeByte(damage, ValueType.S); // damage amount
			writer.writeByte(type, ValueType.A);
		}
	}

}
