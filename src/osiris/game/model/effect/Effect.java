package osiris.game.model.effect;

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

// TODO: Auto-generated Javadoc
/**
 * The Class Effect.
 * 
 * @author Boomer
 */
public abstract class Effect {

	/**
	 * Execute.
	 * 
	 * @param character
	 *            the character
	 */
	public abstract void execute(Character character);

	/**
	 * Equals.
	 * 
	 * @param effect
	 *            the effect
	 * @return true, if successful
	 */
	public abstract boolean equals(Effect effect);

	/**
	 * Update.
	 * 
	 * @param character
	 *            the character
	 */
	public abstract void update(Character character);

	/**
	 * Terminate.
	 * 
	 * @param character
	 *            the character
	 */
	public void terminate(Character character) {

	}
}
