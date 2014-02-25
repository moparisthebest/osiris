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

// TODO: Auto-generated Javadoc

/**
 * The update block for primary damage hits.
 * 
 * @author Blake
 * 
 */
public class PrimaryHitBlock extends UpdateBlock {

	/** The damage. */
	private final int damage;

	/** The type. */
	private final int type;

	/**
	 * Instantiates a new primary hit block.
	 * 
	 * @param character
	 *            the character
	 * @param damage
	 *            the damage
	 * @param type
	 *            the type
	 */
	public PrimaryHitBlock(Character character, int damage, int type) {
		super(character, (character instanceof Npc ? 0x4 : 0x2), (character instanceof Npc ? 7 : 9));
		this.damage = damage;
		this.type = type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see osiris.game.update.UpdateBlock#append(osiris.io.PacketWriter)
	 */

	@Override
	public void append(PacketWriter writer) throws Exception {
		int hp = getCharacter().getCurrentHp();
		if (hp < 0) {
			hp = 0;
		}
		int hpRatio = hp * 255 / getCharacter().getMaxHp();
		if (hpRatio > 255) {
			hpRatio = 255;
		}
		if (getCharacter() instanceof Npc) {
			writer.writeByte(damage);
			writer.writeByte(type);
			writer.writeByte(hpRatio, ValueType.S);
		} else {
			writer.writeByte(damage, ValueType.S);
			writer.writeByte(type, ValueType.S);
			writer.writeByte(hpRatio, ValueType.S);
		}
	}

}
