package osiris.game.model.combat.impl;

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
import osiris.game.model.combat.Attack;
import osiris.game.model.combat.HitType;
import osiris.game.model.magic.MagicManager;
import osiris.game.model.magic.Spell;
import osiris.game.model.magic.SpellBook;

// TODO: Auto-generated Javadoc
/**
 * The Class MagicAttack.
 * 
 * @author Boomer
 */
public class MagicAttack extends Attack {

	/** The spell. */
	private Spell spell;

	/** The defence. */
	private boolean defence;

	/**
	 * Instantiates a new magic attack.
	 * 
	 * @param spell
	 *            the spell
	 */
	public MagicAttack(Spell spell) {
		this(spell, false);
	}

	/**
	 * Instantiates a new magic attack.
	 * 
	 * @param spell
	 *            the spell
	 * @param book
	 *            the book
	 */
	public MagicAttack(int spell, SpellBook book) {
		this(MagicManager.getSpell(spell, book));
	}

	/**
	 * Instantiates a new magic attack.
	 * 
	 * @param spell
	 *            the spell
	 * @param defence
	 *            the defence
	 */
	public MagicAttack(Spell spell, boolean defence) {
		this.spell = spell;
		this.defence = defence;
		setHitType(HitType.MAGIC);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see osiris.game.model.combat.Attack#execute(osiris.game.model.Character,
	 * osiris.game.model.Character, boolean)
	 */
	@Override
	public int execute(Character character, Character victim, boolean random) {
		if (spell == null)
			return -1;
		boolean executed = spell.execute(character, defence, victim);
		if (!executed)
			return -1;
		return spell.getDelay();
	}
}
