package osiris.game.model.combat;

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

// TODO: Auto-generated Javadoc
/**
 * The Class CombatDefaults.
 * 
 * @author Boomer
 * 
 */
public final class CombatDefaults {

	/** The Constant DEFAULT_SPRITES. */
	public static final int[] DEFAULT_ANIMATIONS = { 386, 451, 451, 386 }, DEFAULT_SPRITES = { 0x328, 0x333, 0x338 };

	/** The Constant DEFAULT_TAB. */
	public static final int DEFAULT_TAB = 82;

	/** The Constant DEFAULT_BLOCK. */
	public static final int DEFAULT_BLOCK = 404;

	/** The Constant SPELL_SKILLS. */
	public static final int[][] DEFAULT_SKILLS = new int[][] { { 0 }, { 2 }, { 0, 1, 2 }, { 1 } };

	/** The Constant SWORD_SKILLS. */
	public static final int[][] SWORD_SKILLS = new int[][] { { 0 }, { 2 }, { 2 }, { 1 } };

	/** The Constant RANGE_SKILLS. */
	public static final int[][] RANGE_SKILLS = new int[][] { { 4 }, { 4 }, { 4, 1 } };

	/**
	 * Prevents initilization.
	 */
	private CombatDefaults() {
		// Nothing to do here...
	}

}
