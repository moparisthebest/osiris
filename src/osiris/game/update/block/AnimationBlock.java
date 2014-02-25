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
 * An update block for animations.
 * 
 * @author Blake
 * 
 */
public class AnimationBlock extends UpdateBlock {

	/**
	 * The id.
	 */
	private final int id;

	/**
	 * The delay.
	 */
	private final int delay;

	/**
	 * Instantiates a new animation block.
	 * 
	 * @param character
	 *            the character
	 * @param id
	 *            the id
	 * @param delay
	 *            the delay
	 */
	public AnimationBlock(Character character, int id, int delay) {
		super(character, 0x1, (character instanceof Npc ? 3 : 7));
		this.id = id;
		this.delay = delay;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see osiris.game.update.UpdateBlock#append(osiris.io.PacketWriter)
	 */

	@Override
	public void append(PacketWriter writer) throws Exception {
		if (getCharacter() instanceof Npc) {
			writer.writeShort(id, ValueType.A);
			writer.writeByte(delay);
		} else {
			writer.writeShort(id);
			writer.writeByte(delay, ValueType.S);
		}
	}

	/**
	 * Gets the animation id.
	 * 
	 * @return the animation id
	 */
	public int getAnimationId() {
		return id;
	}

}
