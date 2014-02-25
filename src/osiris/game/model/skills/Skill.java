package osiris.game.model.skills;

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

/**
 * The Class Skill.
 * 
 * @author Boomer
 */
public abstract class Skill {

	/**
	 * Can iterate.
	 * 
	 * @return true, if successful
	 */
	public abstract boolean canIterate();

	/**
	 * Calculate cooldown.
	 * 
	 * @return the int
	 */
	public abstract int calculateCooldown();

	/**
	 * On completion.
	 */
	public void onCompletion() {
	}

}
