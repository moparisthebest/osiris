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

import java.util.BitSet;

// TODO: Auto-generated Javadoc

/**
 * Update flags.
 * 
 * @author Blake
 * 
 */
public class UpdateFlags {

	/**
	 * The flags.
	 */
	private BitSet flags = new BitSet();

	/**
	 * The Enum UpdateFlag.
	 */
	public enum UpdateFlag {

		/**
		 * An update.
		 */
		UPDATE(0),

		/**
		 * A teleport update.
		 */
		TELEPORTED(1),

		/**
		 * No movement queue reset.
		 */
		NO_MOVEMENT_QUEUE_RESET(2);

		/**
		 * The id.
		 */
		private int id;

		/**
		 * Instantiates a new update flag.
		 * 
		 * @param id
		 *            the id
		 */
		UpdateFlag(int id) {
			this.id = id;
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

	/**
	 * Flags an update flag.
	 * 
	 * @param flag
	 *            the flag
	 */
	public void flag(UpdateFlag flag) {
		flags.set(flag.getId(), true);
		flags.set(UpdateFlag.UPDATE.getId(), true);
	}

	/**
	 * Checks if a flag is flagged.
	 * 
	 * @param flag
	 *            the flag
	 * @return true, if the flag is flagged
	 */
	public boolean isFlagged(UpdateFlag flag) {
		return flags.get(flag.getId());
	}

	/**
	 * Reset.
	 */
	public void reset() {
		flags.clear();
	}

}
