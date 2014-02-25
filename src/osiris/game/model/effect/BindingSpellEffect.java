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
 * The Class BindingSpellEffect.
 * 
 * @author Boomer
 */
public class BindingSpellEffect extends BindingEffect {

	/**
	 * Instantiates a new binding spell effect.
	 * 
	 * @param cooldown
	 *            the cooldown
	 * @param attacker
	 *            the attacker
	 * @param duration
	 *            the duration
	 */
	public BindingSpellEffect(Integer cooldown, Character attacker, Object duration) {
		super(cooldown, attacker, duration);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * osiris.game.model.effect.BindingEffect#equals(osiris.game.model.effect
	 * .Effect)
	 */
	@Override
	public boolean equals(Effect effect) {
		return effect instanceof BindingSpellEffect;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * osiris.game.model.effect.BindingEffect#update(osiris.game.model.Character
	 * )
	 */
	@Override
	public void update(Character character) {
	}

}
