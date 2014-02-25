package osiris.game.update;

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
import osiris.io.PacketWriter;

// TODO: Auto-generated Javadoc

/**
 * Represents a block of the update packet.
 * 
 * @author Blake
 * 
 */
public abstract class UpdateBlock {

	/**
	 * The character.
	 */
	private final Character character;

	/**
	 * The mask.
	 */
	private final int mask;

	/**
	 * The priority.
	 */
	private final int priority;

	/**
	 * Instantiates a new update block.
	 * 
	 * @param character
	 *            the character
	 * @param mask
	 *            the mask
	 * @param priority
	 *            the priority
	 */
	public UpdateBlock(Character character, int mask, int priority) {
		this.character = character;
		this.mask = mask;
		this.priority = priority;
	}

	/**
	 * Appends the block to the argued packet writer.
	 * 
	 * @param writer
	 *            the writer
	 * @throws Exception
	 *             the exception
	 */
	public abstract void append(PacketWriter writer) throws Exception;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object other) {
		if (other instanceof UpdateBlock) {
			return ((UpdateBlock) other).getMask() == getMask();
		}
		return false;
	}

	/**
	 * Gets the character.
	 * 
	 * @return the character
	 */
	public Character getCharacter() {
		return character;
	}

	/**
	 * Gets the mask.
	 * 
	 * @return the mask
	 */
	public int getMask() {
		return mask;
	}

	/**
	 * Gets the priority.
	 * 
	 * @return the priority
	 */
	public int getPriority() {
		return priority;
	}

}
