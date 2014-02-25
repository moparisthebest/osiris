package osiris.game.action.impl;

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

import osiris.game.action.Action;
import osiris.game.model.Character;

// TODO: Auto-generated Javadoc
/**
 * The Class CombatAction.
 * 
 * @author Boomer
 */
public class CombatAction extends Action {

	/** The victim. */
	private final Character victim;

	/** The spell slot. */
	private int spellSlot;

	/**
	 * Instantiates a new distanced action.
	 * 
	 * @param character
	 *            the character
	 * @param victim
	 *            the victim
	 */
	public CombatAction(Character character, Character victim) {
		super(character);
		this.victim = victim;
		this.spellSlot = -1;
	}

	/**
	 * Instantiates a new combat action.
	 * 
	 * @param character
	 *            the character
	 * @param victim
	 *            the victim
	 * @param spellSlot
	 *            the spell slot
	 */
	public CombatAction(Character character, Character victim, int spellSlot) {
		super(character);
		this.victim = victim;
		this.spellSlot = spellSlot;
		character.getMovementQueue().reset();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see osiris.game.action.Action#onCancel()
	 */
	@Override
	public void onCancel() {
		getCharacter().getCombatManager().resetAttackVars();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see osiris.game.action.Action#onRun()
	 */
	@Override
	public void onRun() {
		getCharacter().getCombatManager().attackCharacter(victim, spellSlot);
	}
}
