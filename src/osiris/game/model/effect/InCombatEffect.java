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
 * The Class InCombatEffect.
 * 
 * @author Boomer
 */
public class InCombatEffect extends CombatEffect {

	/** The Constant COOLDOWN. */
	public static final int COOLDOWN = 8;

	/**
	 * Instantiates a new in combat effect.
	 * 
	 * @param attacker
	 *            the attacker
	 */
	public InCombatEffect(Character attacker) {
		super(COOLDOWN, attacker);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see osiris.game.model.effect.Effect#execute(osiris.game.model.Character)
	 */
	@Override
	public void execute(Character character) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * osiris.game.model.effect.Effect#equals(osiris.game.model.effect.Effect)
	 */
	@Override
	public boolean equals(Effect effect) {
		return effect instanceof InCombatEffect;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see osiris.game.model.effect.Effect#update(osiris.game.model.Character)
	 */
	@Override
	public void update(Character character) {
	}
}
